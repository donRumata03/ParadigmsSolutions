function namedTreeToStringBuilder(builder, children, name) {
	children.forEach(child => { child.toStringBuilder(builder); builder.push(" "); });
	builder.push(name);
}
function namedTreeToPrefixBuilder(builder, children, name) {
	builder.push("(");
	builder.push(name);
	children.forEach(child => { builder.push(" "); child.toStringBuilder(builder); });
	builder.push(")");
}

function stringify(extendBuilder) {
	let resBuilder = [];
	extendBuilder(resBuilder);
	return resBuilder.join("");
}

let deriveToString = obj => obj.toString = stringify(this.toStringBuilder);
let derivePrefix = obj => obj.prefix = stringify(this.toPrefixBuilder);

let createReductionNode = reductionOp => symbol => {
	let constructor = function (...children) {
		this.op = reductionOp;
		this.name = symbol;
		this.children = children;
	};
	constructor.prototype.arity = reductionOp.length;
	constructor.prototype.evaluate = function (...args) {
		return this.op(...(this.children.map(child => child.evaluate(...args))));
	}
	constructor.prototype.toStringBuilder = function(builder) {
		namedTreeToStringBuilder(builder, this.children, symbol);
	}
	constructor.prototype.toPrefixBuilder = function(builder) {
		namedTreeToPrefixBuilder(builder, this.children, symbol);
	}
	deriveToString(constructor.prototype);

	return constructor;
}


let Const = function (value) {
	this.value = value;
}
Const.prototype.arity = 0;
Const.prototype.toStringBuilder = function (builder) {
	builder.push(this.value.toString());
}
deriveToString(Const.prototype);
Const.prototype.evaluate = function(..._args) {
	return this.value;
}
Const.prototype.diff = function(varName) {
	return new Const(0);
}

let Variable = function (name) {
	this.name = name;
}
Variable.prototype.arity = 0;
Variable.prototype.toStringBuilder = function (builder) {
	builder.push(this.name);
}
deriveToString(Variable.prototype);
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
	newNode.prototype.arity = treeConstructor.length;
	newNode.prototype.diff = function (varName) {
		return this.inner.diff(varName);
	}
	newNode.prototype.evaluate = function (...args) {
		return this.inner.evaluate(...args);
	}
	newNode.prototype.toStringBuilder = function (builder) {
		namedTreeToStringBuilder(builder, this.treeList, this.name);
	}
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
let node = new Gauss(new Const(1), new Const(2), new Const(3), new Const(4));
console.log(node.evaluate(1, 2, 3));
// console.log(node.evaluate(1, 2, 3));
// console.log("«" + node.toString() + "»");
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

// TODO: add tokens «(» and «)»
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

// TODO: add a number parser parsePrefix(string):
//  rawPrefixExpression --> OPERATOR_SYMBOL '(' prefixExpression ')' '(' prefixExpression ')' … '(' prefixExpression ')' prefixExpression
//  prefixExpression --> rawPrefixExpression | '(' rawPrefixExpression ')'
//
// (in rawPrefixExpression definition the argument number should be match the operator's argument number)
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

