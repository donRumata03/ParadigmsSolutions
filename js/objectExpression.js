

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
	};

	constructor.prototype = { op: undefined, name: undefined, children: undefined }
	constructor.prototype.evaluate = function (...args) {
		return this.op(...(this.children.map(child => child.evaluate(...args))));
	}
	constructor.prototype.toStringBuilder = function(builder) {
		for (let i = 0; i < this.children.length; i++) {
			this.children[i].toStringBuilder(builder);
			if (i === 0) builder.push(" ");
		}
		builder.push(symbol);
	}

	deriveToString(constructor.prototype)
	return constructor;
}


let Const = function (value) {
	this.value = value;
}
Const.prototype = { value: 0 };
Const.prototype.toStringBuilder = function (builder) {
	builder.push(this.value.toString());
}
Const.prototype.evaluate = function(..._args) {
	return this.value;
}

deriveToString(Const.prototype);

let Variable = function (name) {
	this.name = name;

	this.toStringBuilder = function (builder) {
		builder.push(name);
	}
	this.evaluate = function(x, y, z) {
		return name === "x" ? x : (name === "y" ? y : z);
	}
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

let node = new Multiply(new Add(new Const(10), new Const(30)), new Const(566));

console.log(node.evaluate(1, 2, 3));
console.log(new Const(10).toString());
console.log(node.toString());

