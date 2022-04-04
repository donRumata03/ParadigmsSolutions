"use strict"

let cnst = value => (_x, _y, _z) => value;
let variable = name => (x, y, z) => name === "x" ? x : (name === "y" ? y : z);

let nAryReductionNode = reductionOp => {
	let res = function (...children) {
		return function (x, y, z) {
			return reductionOp(...(children.map(child => child(x, y, z))));
		};
	}
	res.arity = reductionOp.length;
	return res;

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
	"pi": nAryReductionNode(() => Math.PI),
	"e": nAryReductionNode(() => Math.E),
	"negate": negate,
	"abs": abs,
	"+": add,
	"-": subtract,
	"*": multiply,
	"/": divide,
	"avg3": avg3,
	"iff": iff,
	"med5": med5,
};
let pi = operators.pi();
let e = operators.e();


let lexer = function (string) {
	let ptr = 0;
	let scanWhile = predicate => start => {
		if (start === string.length || !predicate(string[start])) return start;
		return scanWhile(predicate)(start + 1);
	}

	let isDigit = ch => ch.match(/[0-9]/i);
	let isAlpha = ch => ch.toLowerCase().match(/[a-z]/i);
	let isPositiveNumberStart = pos => pos < string.length && isDigit(string[pos]);

	return () => {
		ptr = scanWhile(ch => ch.trim() === '')(ptr)
		if (ptr === string.length) return undefined;

		if (isPositiveNumberStart(ptr) || (string[ptr] === '-' && isPositiveNumberStart(ptr + 1))) {
			// Numbers
			let afterNumberEnd = scanWhile(isDigit)(ptr + 1);
			let res = string.substring(ptr, afterNumberEnd);
			ptr = afterNumberEnd;
			let num = () => cnst(Number.parseInt(res));
			num.arity = 0;
			return num;
		} else if (string[ptr] in operators) {
			// Single-symbol operators
			return operators[string[ptr++]];
		} else if (isAlpha(string[ptr])) {
			// Words
			let afterWordEnd = scanWhile(ch => isAlpha(ch) || isDigit(ch))(ptr + 1);
			let word = string.substring(ptr, afterWordEnd);
			ptr = afterWordEnd;

			if (word in operators) return operators[word];
			let v = () => variable(word);
			v.arity = 0;
			return v;
		} else throw new Error();
	}
}
let mapIterator = f => it => {
	let next = it();
	if (next === undefined) return;
	f(next);
	mapIterator(f)(it);
}

let parse = function (string) {
	let lex = lexer(string)
	let stack = [];

	mapIterator((next) => {
		console.assert(next.arity !== undefined)
		stack.push(next(...stack.splice(stack.length - next.arity, next.arity)));
	})(lex);
	if (stack.length !== 1) throw new Error();
	return stack[0];
}

// let node = parse("x 2 +");
// let lex = lexer("x 2 +");
// let arr = [];
// mapIterator((v) => arr.push(v))(lex)
// console.log(arr);
//
// console.log(node(0, 0, 0));
