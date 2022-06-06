% Generic Utilities

map([], F, []).
map([H | T], F, [HR | TR]) :- Rule =.. [F, H, HR], call(Rule), map(T, F, TR).

range(F, L, []) :- F >= L.
range(F, L, [F | T]) :- F < L, F1 is F + 1, range(F1, L, T).

filter([], _, []).
filter([H | T], P, [H | RT]) :- G =.. [P, H], call(G), !, filter(T, P, RT).
filter([H | T], P, RT) :- filter(T, P, RT).

foldLeft([], Z, _, Z).
foldLeft([H | T], Z, F, R) :- G =.. [F, Z, H, RH], call(G), foldLeft(T, RH, F, R).

forall([], F).
forall([H | T], F) :- G =.. [F, H], call(G), forall(T, F).

mult_conv(A, B, R) :- R is A * B.
multiply(Array, R) :- foldLeft(Array, 1, mult_conv, R).

ascending([]).
ascending([_]).
ascending([F, S | T]) :- 
	F =< S,
	ascending([S | T]).


% Factor table initialization

init(MAX_N) :- init_(2, MAX_N).

init_(N, MAX_N) :- Sq is N * N, Sq > MAX_N, !.
init_(N, MAX_N) :- 
	S is N + 1,
	process_sieve(N, MAX_N),
	init_(S, MAX_N).

process_sieve(N, _) :- composite_table(N), !.
process_sieve(N, MAX_N) :-  Sq is N * N, mark_composite_until(N, Sq, MAX_N).

mark_composite_until(Modulo, Start, MAX_N) :- Start > MAX_N, !.

mark_composite_until(Modulo, Start, MAX_N) :- 
	update_tables(Modulo, Start),
	Next is Start + Modulo,
	mark_composite_until(Modulo, Next, MAX_N).

update_tables(Modulo, Sieved) :- composite_table(Sieved), !.
update_tables(Modulo, Sieved) :- assert(composite_table(Sieved)), assert(factor_table(Sieved, Modulo)).


% Answering queries (primeness, factorization, lcm)

smallest_prime_factor(N, F) :- prime(N), F = N, !.
smallest_prime_factor(N, F) :- factor_table(N, F).

composite(N) :- composite_table(N).
prime(N) :- \+ composite(N).

prime_divisors(N, Divisors) :- ground(Divisors), forall(Divisors, prime), ascending(Divisors), multiply(Divisors, N), !.
prime_divisors(1, []) :- !.
prime_divisors(N, [S | T]) :- ground(N), smallest_prime_factor(N, S), D is div(N, S), prime_divisors(D, T).

lcm(N, M, R) :- prime_divisors(N, NR), prime_divisors(M, MR), list_lcm(NR, MR, R).

list_lcm([], DM, R) :- multiply(DM, R), !.
list_lcm(DN, [], R) :- multiply(DN, R), !.
list_lcm([H | TN], [H | TM], R) :- list_lcm(TN, TM, TR), R is H * TR, !.
list_lcm([HN | TN], [HM | TM], R) :- HN < HM, list_lcm(TN, [HM | TM], TR), R is HN * TR, !.
list_lcm(DN, DM, R) :- list_lcm(DM, DN, R).
