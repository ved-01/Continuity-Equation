from sympy import symbols,Derivative
def derivative(expr,valt):
    t = symbols('t')
    der = Derivative(expr,t).doit().subs(t,valt)

    return int(der)