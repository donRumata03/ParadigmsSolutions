"use strict"

let cnst = value => (_x, _y, _z) => value;
let variable = name => (x, y, z) => name === "x" ? x : (name === "y" ? y : z);

let nAryReductionNode = reductionOp => function (...children) {
	return function (x, y, z) {
		return reductionOp(...(children.map(child => child(x, y, z))));
	};
}


let add = nAryReductionNode((x, y) => x + y);
let subtract = nAryReductionNode((x, y) => x - y);
let multiply = nAryReductionNode((x, y) => x * y);
let divide = nAryReductionNode((x, y) => x / y);
let negate = nAryReductionNode(v => -v) // (child) => (x, y, z) => -child(x, y, z);

let avg3 = nAryReductionNode((a, b, c) => (a + b + c) / 3);
let med5 = nAryReductionNode((a, b, c, d, e) => [a, b, c, d, e].sort((a, b) => a - b)[2]);
let abs = nAryReductionNode((a) => Math.abs(a));
let iff = nAryReductionNode((a, b, c) => a >= 0 ? b : c);


let operators = {
	"pi": [nAryReductionNode(() => Math.PI), 0],
	"e": [nAryReductionNode(() => Math.E), 0],
	"negate": [negate, 1],
	"abs": [abs, 1],
	"+": [add, 2],
	"-": [subtract, 2],
	"*": [multiply, 2],
	"/": [divide, 2],
	"avg3": [avg3, 3],
	"iff": [iff, 3],
	"med5": [med5, 5],
};
let pi = operators.pi[0]();
let e = operators.e[0]();

// let constants = Object.fromEntries(Object.entries({
//     "pi": Math.PI,
//     "e": Math.E
// }).map(([k, v]) => [k, () => v]))

let lexer = function (string) {
	let ptr = 0;
	let scanWhile = predicate => start => {
		let newPtr = start;
		while (newPtr < string.length && predicate(string[newPtr])) newPtr++;
		return newPtr;
	}
	let matchNextPositions = saluteCutting => startPosition => function (...matchers) {
		let tmpPtr = startPosition;
		for (const positionMatcher of matchers) {
			if (tmpPtr === string.length) return saluteCutting;
			if (!positionMatcher(tmpPtr)) return false;
			tmpPtr++;
		}
		return true;
	}
	let positionizeCharPredicate = f => pos => f(string[pos])
	let matchNextChars = saluteCutting => startPosition => (...args) =>
		matchNextPositions(saluteCutting)(startPosition)(...args.map(positionizeCharPredicate))
	let isNonZeroDigit = ch => ch.match(/[1-9]/i);
	let isDigit = ch => ch.match(/[0-9]/i);
	let isAlpha = ch => ch.toLowerCase().match(/[a-z]/i);
	let isPositiveNumberStart = pos =>
		pos < string.length && (
			isNonZeroDigit(string[pos]) ||
			(string[pos] === '0' && matchNextChars(true)(pos + 1)(ch => !isDigit(ch)))
		);

	return () => {
		ptr = scanWhile(ch => ch.trim() === '')(ptr)
		if (ptr === string.length) return undefined;

		if (
			isPositiveNumberStart(ptr)
			|| matchNextPositions(false)(ptr)(positionizeCharPredicate(ch => ch === '-'), isPositiveNumberStart)
		) {
			// Numbers
			let afterNumberEnd = scanWhile(isDigit)(ptr + 1);
			let res = string.substring(ptr, afterNumberEnd);
			ptr = afterNumberEnd;
			return [() => cnst(Number.parseInt(res)), 0];
		} else if (string[ptr] in operators) {
			// Single-symbol operators
			return operators[string[ptr++]];
		} else if (isAlpha(string[ptr])) {
			// Words
			let afterWordEnd = scanWhile(ch => isAlpha(ch) || isDigit(ch))(ptr + 1);
			let word = string.substring(ptr, afterWordEnd);
			ptr = afterWordEnd;

			if (word in operators) return operators[word];
			return [() => variable(word), 0];
		} else throw new Error();
	}
}

let parse = function (string) {
	let lex = lexer(string)
	let stack = [];

	while (true) {
		let next = lex();
		if (next === undefined) break;

		let nodeChildren = [];
		for (let i = 0; i < next[1]; i++) {
			nodeChildren.push(stack.pop());
		}
		stack.push(next[0](...nodeChildren.reverse()));
	}
	if (stack.length !== 1) throw new Error();
	return stack[0];
}
