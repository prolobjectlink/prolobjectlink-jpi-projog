different(X, X) :- ,(!, fail).
different(X, Y).
female(pam).
female(liz).
female(ann).
female(pat).
grandparent(X, Z) :- ,(parent(X, Y), parent(Y, Z)).
male(tom).
male(bob).
male(jim).
mother(X, Y) :- ,(parent(X, Y), female(X)).
offspring(X, Y) :- parent(X, Y).
parent(pam, bob).
parent(tom, bob).
parent(tom, liz).
parent(bob, ann).
parent(bob, pat).
parent(pat, jim).
predecessor(X, Z) :- parent(X, Z).
predecessor(X, Z) :- ,(parent(X, Y), predecessor(Y, Z)).
sister(X, Y) :- ,(,(,(parent(Z, X), parent(Z, Y)), female(X)), different(X, Y)).
