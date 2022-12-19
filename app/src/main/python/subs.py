from sympy import Derivative, symbols
def sub(expr,tval):
    t=symbols('t')
    expr=Derivative(expr,t,0).doit().subs(t,tval)
    return expr