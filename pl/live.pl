% First part of code are «General Utilities» partially copied from lections, 
% the most interesting part is below.
% — Implementation comments are after the «General Utilities» section
% — Run instructions are in the end of file

map([], F, []).
map([H | T], F, [HR | TR]) :- Rule =.. [F, H, HR], call(Rule), map(T, F, TR).

range(F, L, []) :- F >= L.
range(F, L, [F | T]) :- F < L, F1 is F + 1, range(F1, L, T).

filter([], _, []).
filter([H | T], P, [H | RT]) :- G =.. [P, H], call(G), !, filter(T, P, RT).
filter([H | T], P, RT) :- filter(T, P, RT).

concat([], B, B).
concat([H | T], B, [H | R]) :- concat(T, B, R).

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

% Sorting

%negate(Pred) :- \+ call(Pred).
%inverse_predicate(Pred, Res) :- Res = negate(Pred).

%split_by(List, LEq, G, Falses, Trues) :- 
%	filter(List, LEq, Trues),
%	NotPred =.. []

% index_of([1, 2, 3], 2, R)
index_of([Element | T], Element, 0).
index_of([H | T], Element, Index) :- index_of(T, Element, P), Index is P + 1.


% get_nth([1, 2, 3], 2, E)
get_nth([H | T], 0, H).
get_nth([H | T], N, Element) :- P is N - 1, get_nth(T, P, Element).

% select_pivot([1, 2, 3, 4], R)
% It's better than take the first element as pivot and O(N) is ok here
select_pivot(List, Pivot) :- 
	length(List, Len),
	Half is div(Len, 2),
	get_nth(List, Half, Pivot).


% Implemenrtation notes:
% — Indexing 2D lists in Prolog is not the best idea, 
% 		So we'll store the cellular automaton state in list of live(x, y)
% — New generation is produced like this: we generate all points that are members to al least one cell 
% and then filter them by number of neighbors in prev population and by «being alive there» fact
% — The returned value of simulate function is pair of period size, and the last simulation state
% We rteturn this state because it is guaranteed to lie in the cycle found
% — Program doesn't terminate while it doesn't find a cycle.


% alive(WasAlive, NeighborNumber)
alive(was_alive, NeighborNumber) :- NeighborNumber >= 2, NeighborNumber =< 3.
alive(was_dead, 3).

% Cell is denoted as live(x, y)

are_neighbors(live(X1, Y1), live(X2, Y2)) :- 
	\+ (X1, Y1) = (X2, Y2), 
	DX is abs(X1 - X2), DY is abs(Y1 - Y2),
	DX =< 1, DY =< 1.

get_neighbors_and_self(live(X, Y), [
	live(PX, PY), 
	live(PX, Y),
	live(PX, SY),
	
	live(X, PY),
	live(X, Y),	
	live(X, SY),
	
	live(SX, PY),
	live(SX, Y),
	live(SX, SY)
	]) :- PX is X - 1, SX is X + 1, PY is Y - 1, SY is Y + 1.

% shift_points((-1, -2), [live(1, 2), live(3, 4)], R)
shift_points((_, _), [], []).
shift_points((DX, DY), [live(HX, HY) | T], [live(RHX, RHY) | RT]) :- 
	RHX is HX + DX, RHY is HY + DY, shift_points((DX, DY), T, RT).

% normalize_by_head([live(1, 2), live(3, 4)], R)
normalize_by_head([live(X0, Y0) | T], R) :- 
	DX is - X0,
	DY is - Y0,
	print(DX), nl,
	shift_points((DX, DY), [live(X0, Y0) | T], R).

% Sort points lexicographically with removing duplicates
% Sorting particularly points (not generically) because of absence of partial application in Prolog…
% sort_points([live(1, 2), live(2, 3), live(-1, 10), live(2, 3)], R)
sort_points([], []) :- !.
sort_points([E], [E]) :- !.
sort_points(List, Res) :-
	select_pivot(List, Pivot),
	points_smaller_than(List, Pivot, Smaller),
	points_greater_than(List, Pivot, Greater),
	sort_points(Smaller, SortedSmaller),
	sort_points(Greater, SortedGreater),	
	concat(SortedSmaller, [Pivot], Leq),
	concat(Leq, SortedGreater, Res).

cell_g(live(X1, Y1), live(X2, Y2)) :- X1 > X2, !.
cell_g(live(X1, Y1), live(X2, Y2)) :- X1 = X2, Y1 > Y2.
cell_l(C1, C2) :- cell_g(C2, C1).

% points_smaller_than([live(1, 2), live(2, 3), live(-1, 10)], live(2, 1), R)
points_smaller_than([], _, []).
points_smaller_than([H | T], Pivot, [H | RT]) :- cell_g(Pivot, H), !, points_smaller_than(T, Pivot, RT).
points_smaller_than([H | T], Pivot, RT) :- points_smaller_than(T, Pivot, RT).

% points_greater_than([live(1, 2), live(2, 3), live(-1, 10)], live(2, 1), R)
points_greater_than([], _, []).
points_greater_than([H | T], Pivot, [H | RT]) :- cell_l(Pivot, H), !, points_greater_than(T, Pivot, RT).
points_greater_than([H | T], Pivot, RT) :- points_greater_than(T, Pivot, RT).

% normalize_by_corner([live(1, 2), live(2, 3), live(-1, 10), live(2, 3)], R)
normalize_by_corner(Points, R) :- 
	sort_points(Points, Sorted),
	normalize_by_head(Sorted, R).

% generate_children([live(0, 0), live(0, 1)], R), sort_points(R, S)
generate_children([], []) :- !.
generate_children([H | T], Res) :- get_neighbors_and_self(H, HCh), generate_children(T, TCh), concat(HCh, TCh, Res).

% count_neighbors_in(live(-1, -1), [live(-1,-1),live(-1,0),live(-1,1),live(0,-1),live(0,0),live(0,1),live(1,-1),live(1,0),live(1,1)], R)
// :NOTE: Эффективней реализовывать через трехстрочное слияние, когда сливаются строки n, n-1, n+1
// :NOTE: Тогда эта часть будет работать за линейное время
count_neighbors_in(Cell, [], 0) :- !.
count_neighbors_in(Cell, [H | T], Res) :- 
	are_neighbors(Cell, H), !,
	count_neighbors_in(Cell, T, TRes), 
	Res is TRes + 1.
count_neighbors_in(Cell, [H | T], Res) :- 
	count_neighbors_in(Cell, T, Res).

get_live_state(Population, Cell, was_alive) :- member(Cell, Population), !.
get_live_state(Population, Cell, was_dead).


% filter_alive(Prev, Children, NextGeneration)
% get_neighbors_and_self(live(0, 0), NB), filter_alive(NB, [live(0, 0), live(-1, -1), live(10, 10), live(1, 0), live(1, -2), live(0, -2)], Res)
filter_alive(Prev, [], []) :- !.

filter_alive(Prev, [H | T], [H | NT]) :- 
	count_neighbors_in(H, Prev, Cnt),
	get_live_state(Prev, H, PrevState),
	alive(PrevState, Cnt), !, filter_alive(Prev, T, NT).

filter_alive(Prev, [H | T], NT) :- filter_alive(Prev, T, NT). % If not alive


% next_generation([live(0, 0), live(0, 1), live(0, -1)], R)
next_generation(Population, NextGeneration) :- 
	generate_children(Population, Children), 
	filter_alive(Population, Children, Filtered),
	sort_points(Filtered, NextGeneration).


next_gen_accumulate([Prev | T], [NewGen, Prev | T]) :- next_generation(Prev, NewGen).

simulate_with_acc([Prev | T], (NewGen, Period)) :- 
	next_generation(Prev, NewGen),
	member(NewGen, [Prev | T]), !,
	index_of([Prev | T], NewGen, PP),
	Period is PP + 1.

simulate_with_acc([Prev | T], Res) :- 
	next_generation(Prev, NewGen),
	simulate_with_acc([NewGen, Prev | T], Res).


% Example usage:
% simulate([live(-1, 0), live(0, 0), live(1, 0)], Res)
% → ([live(-1,0),live(0,0),live(1,0)], 2)
simulate(Init, Res) :- simulate_with_acc([Init], Res).
