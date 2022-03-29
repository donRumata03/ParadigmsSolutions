

function deriveToString(obj) {
	obj.toString = function () {
		let res = [];
		obj.toStringBuilder(res);
		return res.join("");
	}
	return obj;
}

// TODO: take derivative function of operation itself
//  and then generate «diff» function by multiplying by nested derivatives
let createReductionNode = reductionOp => symbol => {
	let constructor = function (...children) {
		this.op = reductionOp;
		this.name = symbol;
		this.children = children;
		deriveToString(this);
	};

	constructor.prototype = { op: undefined, name: undefined, children: undefined }
	constructor.prototype.evaluate = function (...args) {
		return this.op(...(this.children.map(child => child.evaluate(...args))));
	}
	constructor.prototype.toStringBuilder = function(builder) {
		this.children.forEach(child => { child.toStringBuilder(builder); builder.push(" "); });
		builder.push(symbol);
	}

	return constructor;
}


let Const = function (value) {
	this.value = value;
	deriveToString(this);
}
Const.prototype = { value: 0 };
Const.prototype.toStringBuilder = function (builder) {
	builder.push(this.value.toString());
}
Const.prototype.evaluate = function(..._args) {
	return this.value;
}


let Variable = function (name) {
	this.name = name;
	deriveToString(this);
}
Variable.prototype.toStringBuilder = function (builder) {
	builder.push(name);
}
Variable.prototype.evaluate = function(x, y, z) {
	return name === "x" ? x : (name === "y" ? y : z);
}



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

// let node = new Multiply(new Const(566), new Variable("x"));
//
// console.log(node.evaluate(1, 2, 3));
// console.log(node.evaluate(1, 2, 3));
// console.log("«" + node.toString() + "»");


////////////////////////////////////////////////////////////////////////////
