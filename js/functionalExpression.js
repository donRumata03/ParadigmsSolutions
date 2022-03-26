"use strict"

let cnst = value => (_x, _y, _z) => value
let variable = name => (x, y, z) => name === "x" ? x : (name === "y" ? y : z)

let binaryOp = (op) => (l, r) => (x, y, z) => op(l(x, y, z), r(x, y, z))

let add = binaryOp((x, y) => x + y)
let subtract = binaryOp((x, y) => x - y)
let multiply = binaryOp((x, y) => x * y)
let divide = binaryOp((x, y) => x / y)

let negate = (child) => (x, y, z) => -child(x, y, z)

let lexer = function (string) {
    let ptr = 0;
    let scanWhile = predicate => {
        let newPtr = ptr;
        while (newPtr < string.length && predicate(string[newPtr])) newPtr++;
        return newPtr;
    }
    let operators = {
        "+": add,
        "-": subtract,
        "*": multiply,
        ".": divide
    }
    let isDigit = ch => ch.match(/[1-9]/i);
    let isAlpha = ch => ch.toLowerCase().match(/[a-z]/i);

    return () => {
        ptr = scanWhile(ch => ch.trim() === '')
        if (ptr === string.length) return undefined;

        if (isAlpha(string[ptr])) {
            return variable(string[ptr++]);
        } else if (isDigit(string[ptr])) {
            let afterDigitEnd = scanWhile(isDigit)
            let res = string.substring(ptr, afterDigitEnd);
            ptr = afterDigitEnd;
            return cnst(Number.parseInt(res));
        } else {
            if (!string[ptr] in operators) throw new Error()
            return operators[string[ptr++]];
        }
    }
}
let parse = function (string) {
    let lex = lexer(string)

    while (true) {
        let next = lex();
        if (next === undefined) return;
        console.log(next());
    }
}

parse("x x 2 - * x * 1 +")

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