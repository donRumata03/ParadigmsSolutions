"use strict"

let cnst = value => (_x, _y, _z) => value;
let variable = name => (x, y, z) => name === "x" ? x : (name === "y" ? y : z);

let binaryOp = (op) => (l, r) => (x, y, z) => op(l(x, y, z), r(x, y, z));

let add = binaryOp((x, y) => x + y);
let subtract = binaryOp((x, y) => x - y);
let multiply = binaryOp((x, y) => x * y);
let divide = binaryOp((x, y) => x / y);

let negate = (child) => (x, y, z) => -child(x, y, z);

let binaryOperators = {
    "+": add,
    "-": subtract,
    "*": multiply,
    "/": divide
};
let unaryOperators = {
    "negate": negate
};

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
console.log(parse("-314 negate -819 x - *")(5, 0, 0))
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

