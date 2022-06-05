
person("Arist").

death(A) :- person(A).

dumb_is_prime_(N, CheckedTo) :- N < CheckedTo * CheckedTo. % Assume already checked all divisors in [2; CheckedTo]
dumb_is_prime_(N, CheckedTo) :- N = CheckedTo * CheckedTo.
dumb_is_prime_(N, CheckedTo) :- 
	N > CheckedTo * CheckedTo, 
	C1 is CheckedTo + 1, 
	\+ (0 is mod(N, C1)), 
	dumb_is_prime_(N, C1).

dumb_is_prime(N) :- dumb_is_prime_(N, 1).


% map([1, 2, L], double, [F, S, 6])
map([], F, []).
map([H | T], F, [HR | TR]) :- Rule =.. [F, H, HR], call(Rule), map(T, F, TR).

double(N, R) :- number(N), !, R is N * 2.
double(N, R) :- number(R), !, N is div(R, 2), R is N * 2.

range(F, L, []) :- F >= L.
range(F, L, [F | T]) :- F < L, F1 is F + 1, range(F1, L, T).

% range(2, 20, Range), filter(Range, dumb_is_prime, R)
filter([], _, []).
filter([H | T], P, [H | RT]) :- G =.. [P, H], call(G), !, filter(T, P, RT).
filter([H | T], P, RT) :- filter(T, P, RT).



% Tables

fib_mem(N, R) :- fib_table(N, R), !. % If some, read from table, otherwise — try to derive from previous Ns
fib_mem(1, 1).
fib_mem(2, 1).
fib_mem(N, R) :-
    N > 0,
    N1 is N - 1, fib_mem(N1, R1),
    N2 is N - 2, fib_mem(N2, R2),
    R is R1 + R2,
    assert(fib_table(N, R)). % Write to table

print_join(Arr) :- print_join_(Arr), nl.
print_join_([]).
print_join_([H | T]) :- print(H), print(" "), print_join_(T).

% Primes

init(MAX_N) :- init_(2, MAX_N).

init_(N, MAX_N) :- Sq is N * N, Sq > MAX_N, !.
init_(N, MAX_N) :- 
	S is N + 1, 
	process(N, MAX_N),
	% print_join(["Rec-init ", N]), 
	init_(S, MAX_N).

process(N, _) :- 
	composite_table(N), 
	% print_join(["Process composite: ", N]), 
	!.
process(N, MAX_N) :- 
	% print_join(["Process prime: ", N]	), 
	Sq is N * N, mark_composite_until(N, Sq, MAX_N).

mark_composite_until(Modulo, Start, MAX_N) :- Start > MAX_N, 
	% print_join(["Stop marking composite: ", Modulo, " ", Start, " ", MAX_N]), 
	!.
	
mark_composite_until(Modulo, Start, MAX_N) :- 
	assert(composite_table(Start)), 
	% print_join(["Continue marking composite: ", Modulo, " ", Start, " ", MAX_N]),
	Next is Start + Modulo, 
	mark_composite_until(Modulo, Next, MAX_N).

composite(N) :- composite_table(N).
prime(N) :- \+ composite(N).


prime_divisors(N, Divisors) :- .

count_primes(N, L) :- init(N), N1 is N + 1, range(1, N1, Range), filter(Range, prime, R), length(R, L).
