

function deriveToString(obj) {
	obj.toString = function () {
		let res = [];
		obj.toStringBuilder(res);
		return res.join("");
	}
	return obj;
}

let createReductionNode = reductionOp => symbol => function (...children) {
	this.op = reductionOp;
	this.name = symbol;
	this.children = children;

	this.evaluate = function (...args) {
		return this.op(...(this.children.map(child => child(...args))));
	}
	this.toStringBuilder = function(builder) {
		for (let i = 0; i < children.length; i++) {
			children[i].toStringBuilder(builder);
			if (i === 0) builder.push(" ");
		}
		builder.push(symbol);
	}
}
deriveToString(createReductionNode.prototype)


let Const = function (value) {
	this.value = value;

	this.toStringBuilder = function (builder) {
		builder.push(value.toString());
	}
	this.evaluate = function(..._args) {
		return this.value;
	}
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
deriveToString(Const.prototype);


let Multiply = createReductionNode((x, y) => x * y)("*");
let Add = createReductionNode((x, y) => x + y)("+");

let node = new Multiply(new Add(new Const(10), new Const(30)), new Const(566));

console.log(node.evaluate(1, 2, 3));

