"use strict"

let cnst = value => (_x, _y, _z) => value;
let variable = name => (x, y, z) => name === "x" ? x : (name === "y" ? y : z);

let nAryReductionNode = reductionOp => function (...children) {
	return function (x, y, z) {
		return reductionOp(...(children.map(child => child(x, y, z))));
	};
}

let constNode = () => cnst;
let variableNode = () => variable;

let add = nAryReductionNode((x, y) => x + y);
let subtract = nAryReductionNode((x, y) => x - y);
let multiply = nAryReductionNode((x, y) => x * y);
let divide = nAryReductionNode((x, y) => x / y);
let negate = nAryReductionNode(v => -v) // (child) => (x, y, z) => -child(x, y, z);

let avg3 = nAryReductionNode((a, b, c) => (a + b + c) / 3);
let med5 = nAryReductionNode((a, b, c, d, e) => {
	let srt = [a, b, c, d, e].sort();
	let res = srt[2];
	console.log("Computing med5 of: " + srt.toString() + ", res is: " + res);
	return res;
});


let operators = {
	"pi": [nAryReductionNode(() => Math.PI), 0],
	"e": [nAryReductionNode(() => Math.E), 0],
	"negate": [negate, 1],
	"+": [add, 2],
	"-": [subtract, 2],
	"*": [multiply, 2],
	"/": [divide, 2],
	"avg3": [avg3, 3],
	"med5": [med5, 5]
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
	console.log("Parsing " + string)

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

// console.log(parse("x x 2 - * x * 1 +")(5, 0, 0))
// console.log(parse("323 0 / negate")(5, 0, 0))
// console.log(parse("100000")(0.00000000000000000000,0.00000000000000000000,0.00000000000000000000))
// console.log(parse("-314 negate -819 x - *")(5, 0, 0))
// console.log(parse("pi x +")(5, 0, 0))
// console.log(
// 	med5(avg3(negate(variable('y')), variable('x'), multiply(cnst(-33), med5(variable('x'), variable('z'), med5(variable('z'), variable('x'), variable('x'), pi, variable('y')), e, add(cnst(927), variable('y'))))), variable('y'), subtract(divide(subtract(cnst(-372), variable('y')), multiply(variable('z'), variable('z'))), avg3(negate(pi), variable('z'), variable('x'))), negate(variable('y')), med5(med5(variable('z'), avg3(variable('z'), cnst(468), e), avg3(variable('z'), pi, cnst(997)), variable('x'), cnst(-240)), avg3(avg3(pi, cnst(-859), e), avg3(e, cnst(-49), e), variable('y')), divide(avg3(variable('y'), cnst(449), variable('z')), pi), med5(add(cnst(296), e), multiply(variable('x'), negate(cnst(639))), pi, negate(negate(avg3(divide(variable('y'), e), divide(variable('y'), variable('x')), variable('x')))), avg3(variable('x'), add(e, variable('y')), multiply(variable('z'), divide(cnst(-126), e)))), add(avg3(variable('z'), variable('y'), e), cnst(719))))
// 	(0.68864586032785150000,0.42444473032207786000,0.27974754266318080000))

console.log(parse("1 2 -10 100 -100 med5")(5, 0, 0))
console.log(parse("1 2 3 avg3")(5, 0, 0))
console.log(parse("pi")(5, 0, 0))
console.log(parse("e")(5, 0, 0))

// let expr = subtract(
//     multiply(
//         cnst(2),
//         variable("x")
//     ),
//     cnst(3)
// );
//
// console.log(expr(5, 0, 0));

