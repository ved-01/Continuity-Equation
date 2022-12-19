from sympy import symbols,Derivative
def divergence(expr,valx,valy,valz):
    x, y,z = symbols("x y z")
    var=["x","y","z"]
    l=expr.split(" ")
    div=0
    for i in range(len(l)):
        div+=Derivative(l[i],var[i]).doit().subs([(x,valx),(y,valy),(z,valz)])

    return int(div)