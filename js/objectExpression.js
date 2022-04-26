"use strict"

let joinBuilder = (builder, children, extend) => {
	for (let i = 0; i < children.length; i++) {
		if (i !== 0) {
			builder.push(" ");
		}
		extend(children[i], builder);
	}
}

let polishTreeFormatter = (operatorSymbol, children, builder, formatChild, _isNullary) => {
	children.forEach(child => { formatChild(child, builder); builder.push(" "); });
	builder.push(operatorSymbol);
}
function withParenthesesIfNotNullary(b, c, ext, isNullary) {
	if (!isNullary) {
		b.push("(");
	}
	ext();
	if (!isNullary) {
		b.push(")");
	}
}
let prefixTreeFormatter = (operatorSymbol, children, builder, formatChild, isNullary) => {
	withParenthesesIfNotNullary(builder, children, () => {
		builder.push(operatorSymbol);
		if (!isNullary) builder.push(" "); // Why?…
		joinBuilder(builder, children, formatChild);
	}, isNullary);
}
let postfixTreeFormatter = (operatorSymbol, children, builder, formatChild, isNullary) => {
	withParenthesesIfNotNullary(builder, children, () => {
		joinBuilder(builder, children, formatChild);
		if (!isNullary) builder.push(" ");
		builder.push(operatorSymbol);
	}, isNullary);
}

// function namedTreeToStringBuilder(builder, children, name) {
// 	children.forEach(child => { child.toStringBuilder(builder); builder.push(" "); });
// 	builder.push(name);
// }
// function namedTreeToPrefixBuilder(builder, children, name) {
// 	builder.push("(");
// 	builder.push(name);
// 	children.forEach(child => { builder.push(" "); child.toPrefixBuilder(builder); });
// 	builder.push(")");
// }

function stringify(extendBuilder) {
	let resBuilder = [];
	extendBuilder(resBuilder);
	return resBuilder.join("");
}

let deriveTreeFormatting = function (obj, isNullary) {
	obj.toXBuilder = function (b, formatter) {
		formatter(this.getSymbol(), this.getChildren(), b, (c, b) => c.toXBuilder(b, formatter), isNullary);
	};
	obj.toX = function (formatter) {
		return stringify(b => this.toXBuilder(b, formatter));
	};
	obj.toString = function() { return this.toX(polishTreeFormatter); };
	obj.prefix = function() { return this.toX(prefixTreeFormatter); };
	obj.postfix = function() { return this.toX(postfixTreeFormatter); };
}

function deriveComputingFromInner(obj) {
	obj.evaluate = function (...args) {
		return this.inner.evaluate(...args);
	}
	obj.diff = function (varName) {
		return this.inner.diff(varName);
	}
}

let createReductionNode = reductionOp => symbol => {
	let constructor = function (...children) {
		this.children = children;
	};
	constructor.prototype.name = symbol;
	constructor.prototype.op = reductionOp;
	constructor.arity = reductionOp.length; // Arity is a constructor's, not prototype's property
	constructor.prototype.evaluate = function (...args) {
		return this.op(...(this.children.map(child => child.evaluate(...args))));
	}
	constructor.prototype.getChildren = function() { return this.children; };
	constructor.prototype.getSymbol = function() { return this.name; };

	deriveTreeFormatting(constructor.prototype, constructor.arity === 0);

	return constructor;
}

class Const {
	constructor(value) {
		this.value = value;
	}
	getChildren() { return []; };
	getSymbol() { return this.value.toString(); };
	evaluate(..._args) {
		return this.value;
	}
	diff(_varName) {
		return new Const(0);
	}
}
deriveTreeFormatting(Const.prototype, true);


class Variable {
	constructor (name) {
		this.name = name;
	}
	getChildren() { return []; }
	getSymbol() { return this.name; };
	evaluate(x, y, z) {
		return this.name === "x" ? x : (this.name === "y" ? y : z);
	}
	diff(varName) {
		return new Const(
			(varName === this.name) ? 1 : 0
		);
	}
}
deriveTreeFormatting(Variable.prototype, true);


let Add = createReductionNode((x, y) => x + y)("+");
let Subtract = createReductionNode((x, y) => x - y)("-");
let Multiply = createReductionNode((x, y) => x * y)("*");
let Divide = createReductionNode((x, y) => x / y)("/");
let Negate = createReductionNode((x) => -x)("negate");
let Exponentiate = createReductionNode(Math.exp)("exp");


Add.prototype.diff = function (varName) {
	return new Add(
		this.children[0].diff(varName),
		this.children[1].diff(varName)
	);
}
Subtract.prototype.diff = function (varName) {
	return new Subtract(
		this.children[0].diff(varName),
		this.children[1].diff(varName)
	);
}
Multiply.prototype.diff = function (varName) {
	const l = this.children[0];
	const r = this.children[1];
	return new Add(
		new Multiply(l, r.diff(varName)),
		new Multiply(l.diff(varName), r)
	);
}
Divide.prototype.diff = function (varName) {
	const l = this.children[0];
	const r = this.children[1];
	return new Divide(
		new Subtract(
			new Multiply(l.diff(varName), r),
			new Multiply(l, r.diff(varName))
		),
		new Multiply(r, r)
	);
}

Negate.prototype.diff = function (varName) {
	return new Negate(this.children[0].diff(varName));
}

Exponentiate.prototype.diff = function (varName) {
	return new Multiply(this, this.children[0].diff(varName));
}

let withArity = (f, arity) => { f.arity = arity; return f; };
let withAutoArity = f => withArity(f, f.length);

function labelParametrizedTree(treeConstructor, label) {
	let newNode = function (...trees) {
		this.treeList = Array.from(trees);
		this.inner = treeConstructor(...trees);
	};
	newNode.prototype.name = label;
	newNode.arity = treeConstructor.arity;
	newNode.prototype.getChildren = function() { return this.treeList; };
	newNode.prototype.getSymbol = function() { return this.name; };
	deriveTreeFormatting(newNode.prototype, false);
	deriveComputingFromInner(newNode.prototype);

	return newNode;
}

/**
 * Internally, constructs folding tree
 * Uses it for evaluation and differentiation
 * But overloads displaying as flat vararg expression
 */
function foldifyBinaryOperator(nodeConstructor, neutralConstNode, label) {
	let constructor = function (...children) {
		this.treeList = Array.from(children);
		this.inner = children.length === 0 ? neutralConstNode :
			children.reduce((previousTree, currentNode) => new nodeConstructor(previousTree, currentNode));
	};
	constructor.prototype.name = label;
	constructor.arity = Infinity;
	constructor.prototype.getChildren = function() { return this.treeList; };
	constructor.prototype.getSymbol = function() { return this.name; };

	deriveTreeFormatting(constructor.prototype, false);
	deriveComputingFromInner(constructor.prototype);

	return constructor;
}

let Gauss = labelParametrizedTree(
	withAutoArity((a, b, c, x) => {
		let shift = new Subtract(x, b);
		return new Multiply(a, new Exponentiate(
			new Negate(new Divide(
				new Multiply(shift, shift),
				new Multiply(new Const(2), new Multiply(c, c))
			)))
		)
	}),
	"gauss"
);

let Sum = foldifyBinaryOperator(Add, new Const(0), "sum"); // (sum … … …)
let Sumexp = labelParametrizedTree(withArity((...trees) => new Sum(...trees.map(t => new Exponentiate(t))), Infinity), "sumexp");
let Softmax = labelParametrizedTree(withArity((...trees) => new Divide(new Exponentiate(trees[0]), new Sumexp(...trees)), Infinity), "softmax");

let sumNode = new Sum(new Const(10), new Variable("x"), new Multiply(new Const(11), new Const(13)));
let sumExpNode = new Sumexp(new Const(10), new Variable("x"), new Multiply(new Const(11), new Const(13)));
let softmaxNode = new Softmax(new Variable("x"), new Const(2), new Const(3));


////////////////////////////////////////////////////////////////////////////
let operators = {
	"negate": Negate,
	"+": Add,
	"-": Subtract,
	"*": Multiply,
	"/": Divide,
	"gauss": Gauss,
	"sumexp": Sumexp,
	"softmax": Softmax,
};

let allowedVariableNames = [
	"x", "y", "z"
];

// There are some issues with displaying true class name through graal


let ParseError = (() => {
	function ParseError(message) {
		this.message = message;
	}

	ParseError.prototype = Object.create(Error.prototype);
	ParseError.prototype.name = "ParseError";
	ParseError.prototype.constructor = ParseError;

	return ParseError;
})();

let TokenizeError = (() => {
	function TokenizeError(message) {
		this.message = message;
	}
	TokenizeError.prototype = Object.create(Error.prototype);
	TokenizeError.prototype.name = "TokenizeError";
	TokenizeError.prototype.constructor = TokenizeError;
	return TokenizeError;
})();

let Lexer = function (string) {
	let ptr = 0;
	let scanWhile = predicate => start => {
		if (start === string.length || !predicate(string[start])) return start;
		return scanWhile(predicate)(start + 1);
	}

	let isDigit = ch => !!([!0, !0, !0, !0, !0, !0, !0, !0, !0, !0][ch]);
	let isAlpha = ch => ch.length === 1 && ch.toLowerCase() !== ch.toUpperCase();
	let skipSpaces = () => ptr = scanWhile(ch => ch.trim() === '')(ptr);
	let isPositiveNumberStart = pos => pos < string.length && isDigit(string[pos]);
	let nullaryWithArity = (constructedNode) => { let res = function() { return constructedNode; }; res.arity = 0; return res; }

	let lexer = () => {
		skipSpaces();
		if (ptr === string.length) return undefined;

		if (isPositiveNumberStart(ptr) || (string[ptr] === '-' && isPositiveNumberStart(ptr + 1))) {
			// Numbers
			let afterNumberEnd = scanWhile(isDigit)(ptr + 1);
			let res = string.substring(ptr, afterNumberEnd);
			ptr = afterNumberEnd;
			return nullaryWithArity(new Const(Number.parseInt(res)));
		} else if (string[ptr] in operators) {
			// Single-symbol operators
			return operators[string[ptr++]];
		} else if (['(', ')'].includes(string[ptr])) {
			return string[ptr++];
		} else if (isAlpha(string[ptr])) {
			// Words
			let afterWordEnd = scanWhile(ch => isAlpha(ch) || isDigit(ch))(ptr + 1);
			let word = string.substring(ptr, afterWordEnd);
			ptr = afterWordEnd;
			if (word in operators) return operators[word];

			if (!(allowedVariableNames.includes(word))) {
				throw new TokenizeError(
					"I've just seen a word „" + word + "“ at position " + ptr +
					". It isn't an operator name and variables with this name are also not allowed");
			}
			return nullaryWithArity(new Variable(word));
		} else {
			throw new TokenizeError("At position " + ptr + " there's nor a word neither { an operator, a number, parentheses }");
		}
	};
	let nextIs = symbol => () => {
		skipSpaces();
		return ptr !== string.length && string[ptr] === symbol;
	};
	lexer.nextIsOpeningParentheses = nextIs('(');
	lexer.nextIsClosingParentheses = nextIs(')');
	return lexer;
}
let mapTokenStream = (f, it) => {
	let next = it();
	if (next === undefined) return;
	f(next);
	mapTokenStream(f, it);
}

let parse = function (string) {
	let lex = Lexer(string)
	let stack = [];

	mapTokenStream((next) => {
		console.assert(next.arity !== undefined)
		stack.push(new next(...stack.splice(stack.length - next.arity, next.arity)));
	}, lex);
	if (stack.length !== 1) {
		console.log(stack.length); throw new Error(); }
	return stack[0];
}

// Grammar:
//      operatorPrefixExpression --> OPERATOR_SYMBOL [ prefixExpression ]+
//      prefixExpression --> '(' operatorPrefixExpression ')' | NULLARY_OPERATOR
// (in rawPrefixExpression definition the argument number should match the operator's argument number)

let formatToken = token => {
	if (token === undefined) {return String.raw`<EOF>`; }

	if (token instanceof Object) {
		if ("getSymbol" in token) {
			return token.getSymbol();
		} else if ("prototype" in token && token.prototype instanceof Object && "getSymbol" in token.prototype) {
			// Feeling the atmosphere of JS…
			return token.prototype.getSymbol();
		}
	}
	return token.toString();
}
let checkHasArity = token => { if (token === undefined || token.arity === undefined) { throw new ParseError("Token " + formatToken(token) + " can't be an operator but stays at its place"); } }

function unexpectedToken(expected, actual, context = undefined) {
	throw new ParseError("Bad token" + (context !== undefined ? " at " + context : "") + ". Expected: " + expected + ", actual: „" + formatToken(actual) + "“");
}
function expectClosingParentheses(lexer, context = undefined) {
	if (!lexer.nextIsClosingParentheses()) {
		unexpectedToken(')', lexer(), context);
	}
	lexer();
}

function parseOperatorTokenizedStream(stream, firstIsFunction) {
	let children = [];
	// for (let i = 0; i < operator.arity; i++) {
	while (!stream.nextIsClosingParentheses()) {
		children.push(parseTokenizedStream(stream, firstIsFunction));
	}


	let operator = stream();
	checkHasArity(operator);
	if (operator.arity === 0) {
		throw new ParseError("Can't use nullary operator as a normal one… Don't know why… 😠");
	}


	let validateArity = (expected, actual) =>
		expected === Infinity
		|| actual === expected
		|| (Array.isArray(expected) && expected[0] <= actual && actual <= expected[1]);

	if (!validateArity(operator.arity, children.length)) {
		throw new ParseError(
			operator.arity + " arguments expected for operator " + formatToken(operator) +
			", actual number of arguments: " + children.length);
	}

	return new operator(...(firstIsFunction ? children.reverse() : children));
}
function parseTokenizedStream(stream, firstIsFunction) {
	if (stream.nextIsOpeningParentheses()) {
		stream();
		let res = parseOperatorTokenizedStream(stream, firstIsFunction);
		expectClosingParentheses(stream, "operator expression ending");
		return res;
	} else {
		let nextNullary = stream();
		checkHasArity(nextNullary);
		if (nextNullary.arity !== 0) {
			unexpectedToken("nullary operator or expression in parentheses", nextNullary, "regular parsing");
		}
		return new nextNullary();
	}
}
function parseXFix(string, functionIsFirst) {
	let lex = Lexer(string);
	let parsed = parseTokenizedStream(lex, functionIsFirst);

	if (lex() !== undefined) {
		throw new ParseError("Couldn't parse the whole expression…");
	}
	return parsed;
}

let parsePrefix = string => parseXFix(Lexer(string), true);
let parsePostfix = string => parseXFix(Lexer(string), false);
