"use strict"

let cnst = value => (_x, _y, _z) => value
let variable = name => (x, y, z) => name === "x" ? x : (name === "y" ? y : z)

let binaryOp = (op) => (l, r) => (x, y, z) => op(l(x, y, z), r(x, y, z))

let add = binaryOp((x, y) => x + y)
let subtract = binaryOp((x, y) => x - y)
let multiply = binaryOp((x, y) => x * y)
let divide = binaryOp((x, y) => x / y)

let negate = (child) => (x, y, z) => -child(x, y, z)


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