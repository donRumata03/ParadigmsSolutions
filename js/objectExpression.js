

function deriveToString(obj) {
	obj.toString = function () {
		let res = [];
		this.toStringBuilder(res);
		return res.join("");
	}
}

// TODO: take derivative function of operation itself
//  and then generate «diff» function by multiplying by nested derivatives
let createReductionNode = reductionOp => symbol => {
	let constructor = function (...children) {
		this.op = reductionOp;
		this.name = symbol;
		this.children = children;
	};

	constructor.prototype.evaluate = function (...args) {
		return this.op(...(this.children.map(child => child.evaluate(...args))));
	}
	constructor.prototype.toStringBuilder = function(builder) {
		this.children.forEach(child => { child.toStringBuilder(builder); builder.push(" "); });
		builder.push(symbol);
	}
	deriveToString(constructor.prototype);

	return constructor;
}


let Const = function (value) {
	this.value = value;
}
Const.prototype.toStringBuilder = function (builder) {
	builder.push(this.value.toString());
}
Const.prototype.evaluate = function(..._args) {
	return this.value;
}
deriveToString(Const.prototype);


let Variable = function (name) {
	this.name = name;
}
Variable.prototype.toStringBuilder = function (builder) {
	builder.push(this.name);
}
Variable.prototype.evaluate = function(x, y, z) {
	return this.name === "x" ? x : (this.name === "y" ? y : z);
}
deriveToString(Variable.prototype);



// TODO: add function to produce aliases for custom expressions:
// TODO: It takes name and expression tree and somehow says the number of arguments
// TODO: for example — Gauss of 4 arguments is a tree
// TODO: When gauss is given the arguments,
//       its evaluation and differentiation is equivalent to expression with a_s and b_s substituted
//       Whereas toString yields children with name

let Multiply = createReductionNode((x, y) => x * y)("*");
let Add = createReductionNode((x, y) => x + y)("+");
let Subtract = createReductionNode((x, y) => x - y)("-");
let Divide = createReductionNode((x, y) => x / y)("/");
let Negate = createReductionNode((x) => -x)("negate");

let node = new Multiply(new Const(566), new Variable("x"));
//
console.log(node.evaluate(1, 2, 3));
console.log(node.evaluate(1, 2, 3));
console.log("«" + node.toString() + "»");


////////////////////////////////////////////////////////////////////////////
let operators = {
	"negate": [Negate, 1],
	"+": [Add, 2],
	"-": [Subtract, 2],
	"*": [Multiply, 2],
	"/": [Divide, 2],
};

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
			return [function() { return new Const(Number.parseInt(res)) }, 0];
		} else if (string[ptr] in operators) {
			// Single-symbol operators
			return operators[string[ptr++]];
		} else if (isAlpha(string[ptr])) {
			// Words
			let afterWordEnd = scanWhile(ch => isAlpha(ch) || isDigit(ch))(ptr + 1);
			let word = string.substring(ptr, afterWordEnd);
			ptr = afterWordEnd;

			if (word in operators) return operators[word];
			return [function() { return new Variable(word) }, 0];
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
		stack.push(new next[0](...nodeChildren.reverse()));
	}
	if (stack.length !== 1) throw new Error();
	return stack[0];
}

