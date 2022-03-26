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

let avg3 = nAryReductionNode((a, b, c) => [a, b, c].reduce((l, r) => l + r) / 3);
let med5 = nAryReductionNode((a, b, c, d, e) => [a, b, c, d, e].sort()[2]);


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

let constants = Object.fromEntries(Object.entries({
    "pi": Math.PI,
    "e": Math.E
}).map(([k, v]) => [k, () => v]))

let lexer = function (string) {
    let ptr = 0;
    let scanWhile = predicate => start => {
        let newPtr = start;
        while (newPtr < string.length && predicate(string[newPtr])) newPtr++;
        return newPtr;
    }
    let matchNextChars = function (...args) {
        let tmpPtr = ptr;
        for (const matcher of args) {
            if (tmpPtr === string.length || !matcher(string[tmpPtr])) return false;
            tmpPtr++;
        }
        return true;
    }
    let isDigit = ch => ch.match(/[0-9]/i);
    let isAlpha = ch => ch.toLowerCase().match(/[a-z]/i);

    return () => {
        ptr = scanWhile(ch => ch.trim() === '')(ptr)
        if (ptr === string.length) return undefined;

        if (isDigit(string[ptr]) || matchNextChars(ch => isDigit(ch) || ch === '-', isDigit)) {
            let afterNumberEnd = scanWhile(isDigit)(ptr + 1);
            let res = string.substring(ptr, afterNumberEnd);
            ptr = afterNumberEnd;
            return cnst(Number.parseInt(res));
        } else if (string[ptr] in binaryOperators) {
            return binaryOperators[string[ptr++]];
        } else if (isAlpha(string[ptr])) {
            let afterWordEnd = scanWhile(isAlpha)(ptr);
            let word = string.substring(ptr, afterWordEnd);
            ptr = afterWordEnd;
            if (word in unaryOperators) return unaryOperators[word];
            return variable(word);
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

        if (Object.values(unaryOperators).includes(next)) {
            stack.push(next(stack.pop()))
        } else if (Object.values(binaryOperators).includes(next)) {
            let r = stack.pop()
            let l = stack.pop();
            stack.push(next(l, r));
        } else {
            stack.push(next);
        }
    }
    if (stack.length !== 1) throw new Error();
    return stack[0];
}

// console.log(parse("x x 2 - * x * 1 +")(5, 0, 0))
// console.log(parse("100000")(0.00000000000000000000,0.00000000000000000000,0.00000000000000000000))
// console.log(parse("-314 negate -819 x - *")(5, 0, 0))
//
// let expr = subtract(
//     multiply(
//         cnst(2),
//         variable("x")
//     ),
//     cnst(3)
// );
//
// console.log(expr(5, 0, 0));

