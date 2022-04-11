let polishTreeFormatter = (operatorSymbol, children, builder, formatChild) => {
	children.forEach(child => { formatChild(child, builder); builder.push(" "); });
	builder.push(operatorSymbol);
}
let prefixTreeFormatter = (operatorSymbol, children, builder, formatChild) => {
	builder.push("(");
	builder.push(operatorSymbol);
	children.forEach(child => { builder.push(" "); formatChild(child, builder); });
	builder.push(")");
}
let postfixTreeFormatter = (operatorSymbol, children, builder, formatChild) => {
	builder.push("(");
	children.forEach(child => { formatChild(child, builder); builder.push(" "); });
	builder.push(operatorSymbol);
	builder.push(")");
}

function namedTreeToStringBuilder(builder, children, name) {
	children.forEach(child => { child.toStringBuilder(builder); builder.push(" "); });
	builder.push(name);
}
function namedTreeToPrefixBuilder(builder, children, name) {
	builder.push("(");
	builder.push(name);
	children.forEach(child => { builder.push(" "); child.toPrefixBuilder(builder); });
	builder.push(")");
}

function stringify(extendBuilder) {
	let resBuilder = [];
	extendBuilder(resBuilder);
	return resBuilder.join("");
}

// «This» in JS is such a gotcha…
let deriveToString = obj => obj.toString = function() { return stringify((b) => this.toStringBuilder(b)); };
let derivePrefix = obj => obj.prefix = function() { return stringify((b) => this.toPrefixBuilder(b)); };

let createReductionNode = reductionOp => symbol => {
	let constructor = function (...children) {
		this.op = reductionOp;
		this.name = symbol;
		this.children = children;
	};
	constructor.arity = reductionOp.length; // Arity is a constructor's, not prototype's property
	constructor.prototype.evaluate = function (...args) {
		return this.op(...(this.children.map(child => child.evaluate(...args))));
	}
	constructor.prototype.toStringBuilder = function(builder) {
		namedTreeToStringBuilder(builder, this.children, symbol);
	}
	constructor.prototype.toPrefixBuilder = function(builder) {
		namedTreeToPrefixBuilder(builder, this.children, symbol);
	}
	derivePrefix(constructor.prototype);
	deriveToString(constructor.prototype);

	return constructor;
}


let Const = function (value) {
	this.value = value;
}
Const.prototype.toStringBuilder = function (builder) {
	builder.push(this.value.toString());
}
deriveToString(Const.prototype);
Const.prototype.toPrefixBuilder = Const.prototype.toStringBuilder;
derivePrefix(Const.prototype);
Const.prototype.evaluate = function(..._args) {
	return this.value;
}
Const.prototype.diff = function(varName) {
	return new Const(0);
}

let Variable = function (name) {
	this.name = name;
}
Variable.prototype.toStringBuilder = function (builder) {
	builder.push(this.name);
}
deriveToString(Variable.prototype);
Variable.prototype.toPrefixBuilder = Variable.prototype.toStringBuilder;
derivePrefix(Variable.prototype);

Variable.prototype.evaluate = function(x, y, z) {
	return this.name === "x" ? x : (this.name === "y" ? y : z);
}
Variable.prototype.diff = function(varName) {
	return new Const(
		(varName === this.name) ? 1 : 0
	);
}




// TODO: add function to produce aliases for custom expressions:
// TODO: It takes name and expression tree and somehow says the number of arguments
// TODO: for example — Gauss of 4 arguments is a tree
// TODO: When gauss is given the arguments,
//       its evaluation and differentiation is equivalent to expression with a_s and b_s substituted
//       Whereas toString yields children with name

let Add = createReductionNode((x, y) => x + y)("+");
let Subtract = createReductionNode((x, y) => x - y)("-");
let Multiply = createReductionNode((x, y) => x * y)("*");
let Divide = createReductionNode((x, y) => x / y)("/");
let Negate = createReductionNode((x) => -x)("negate");
let Exponentiate = createReductionNode((x) => Math.exp(x))("exp");


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


function labelParametrizedTree(treeConstructor, label) {
	let newNode = function (...trees) {
		this.treeList = Array.from(trees);
		this.inner = treeConstructor(...trees);
		this.name = label;
	};
	newNode.arity = treeConstructor.length;
	newNode.prototype.diff = function (varName) {
		return this.inner.diff(varName);
	}
	newNode.prototype.evaluate = function (...args) {
		return this.inner.evaluate(...args);
	}
	newNode.prototype.toStringBuilder = function (builder) {
		namedTreeToStringBuilder(builder, this.treeList, this.name);
	}
	newNode.prototype.toPrefixBuilder = function (builder) {
		namedTreeToPrefixBuilder(builder, this.treeList, this.name);
	}
	derivePrefix(newNode.prototype);
	deriveToString(newNode.prototype);

	return newNode;
}

let Gauss = labelParametrizedTree(
	(a, b, c, x) => {
		let shift = new Subtract(x, b);
		return new Multiply(a, new Exponentiate(
			new Negate(new Divide(
				new Multiply(shift, shift),
				new Multiply(new Const(2), new Multiply(c, c))
			)))
		)
	},
	"gauss"
);

// let node = new Multiply(new Const(566), new Variable("x"));
// let node = new Gauss(new Const(1), new Const(2), new Const(3), new Const(4));
// console.log(node.evaluate(1, 2, 3));
// console.log("«" + node.toString() + "»");
// console.log(node.evaluate(1, 2, 3));
// console.log("«" + node.diff("x").toString() + "»");



////////////////////////////////////////////////////////////////////////////
let operators = {
	"negate": Negate,
	"+": Add,
	"-": Subtract,
	"*": Multiply,
	"/": Divide,
	"gauss": Gauss,
};

let allowedVariableNames = [
	"x", "y", "z"
];

function ParseError(message) {
	this.message = message;
}
ParseError.prototype = Object.create(Error.prototype);
ParseError.prototype.name = "ParseError";
ParseError.prototype.constructor = ParseError;

function TokenizeError(message) {
	this.message = message;
}
TokenizeError.prototype = Object.create(Error.prototype);
TokenizeError.prototype.name = "TokenizeError";
TokenizeError.prototype.constructor = TokenizeError;


let Lexer = function (string) {
	let ptr = 0;
	let scanWhile = predicate => start => {
		if (start === string.length || !predicate(string[start])) return start;
		return scanWhile(predicate)(start + 1);
	}

	let isDigit = ch => ch.match(/[0-9]/i);
	let isAlpha = ch => ch.toLowerCase().match(/[a-z]/i);
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
let mapIterator = f => it => {
	let next = it();
	if (next === undefined) return;
	f(next);
	mapIterator(f)(it);
}

let parse = function (string) {
	let lex = Lexer(string)
	let stack = [];

	mapIterator((next) => {
		console.assert(next.arity !== undefined)
		stack.push(new next(...stack.splice(stack.length - next.arity, next.arity)));
	})(lex);
	if (stack.length !== 1) {
		console.log(stack.length); throw new Error(); }
	return stack[0];
}

// Grammar:
//      operatorPrefixExpression --> OPERATOR_SYMBOL [ prefixExpression ]+
//      prefixExpression --> '(' operatorPrefixExpression ')' | NULLARY_OPERATOR
// (in rawPrefixExpression definition the argument number should match the operator's argument number)

let undefinedIntoEOF = token => (token === undefined) ? String.raw`<EOF>` : token.toString()
let checkHasArity = token => { if (token === undefined || token.arity === undefined) { throw new ParseError("Token " + undefinedIntoEOF(token) + " can't be an operator but stays at its place"); } }

function unexpectedToken(expected, actual, context = undefined) {
	throw new ParseError("Bad token" + (context !== undefined ? " at " + context : "") + ". Expected: " + expected + ", actual: „" + undefinedIntoEOF(actual) + "“");
}
function expectClosingParentheses(lexer, context = undefined) {
	if (!lexer.nextIsClosingParentheses()) {
		unexpectedToken(')', lexer(), context);
	}
	lexer();
}

function parseOperatorTokenizedPrefix(lexer) {
	let operator = lexer();
	checkHasArity(operator);
	if (operator.arity === 0) {
		throw new ParseError("Can't use nullary operator as a normal one… Don't know why…");
	}

	let arguments = [];
	for (let i = 0; i < operator.arity; i++) {
		arguments.push(parseTokenizedPrefix(lexer));
	}

	return new operator(...arguments);
}
function parseTokenizedPrefix(lexer) {
	if (lexer.nextIsOpeningParentheses()) {
		lexer();
		let res = parseOperatorTokenizedPrefix(lexer);
		expectClosingParentheses(lexer, "operator expression ending");
		return res;
	} else {
		let nextNullary = lexer();
		checkHasArity(nextNullary);
		if (nextNullary.arity !== 0) {
			unexpectedToken("nullary operator or expression in parentheses", nextNullary, "regular parsing");
		}
		return new nextNullary();
	}
}
function parsePrefix(string) {
	let lexer = Lexer(string);

	let parsed;
	try {
		parsed = parseTokenizedPrefix(lexer);
	} catch(e) {
		console.log(e);
		console.log(string);
		throw e;
	}

	if (lexer() !== undefined) {
		throw new ParseError("Couldn't parse the whole expression…");
	}
	return parsed;
}


// let emptyInput = parsePrefix("");
let nullaryWith0Args = parsePrefix("1");

// TODO: idea - parse postfix from right to left?
