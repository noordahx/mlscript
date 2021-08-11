
def x = 1
//│ x: 1

1
//│ res: 1

1;
2
//│ res: 1
//│ res: 2

def x = 1;
2
//│ x: 1
//│ res: 2

def x = 1;;
2
//│ x: 1
//│ res: 2

def x = 1
def y = 2
//│ x: 1
//│ y: 2

1
2
3
//│ res: 1
//│ res: 2
//│ res: 3

1
def x = add x 1
//│ res: 1
//│ x: int

def x = 1
x
def y = 2
y
def z = add x y
//│ x: 1
//│ res: 1
//│ y: 2
//│ res: 2
//│ z: int

:e
1
 2
//│ ╔══[ERROR] Type mismatch in application:
//│ ║  l.52: 	1
//│ ║        	^
//│ ║  l.53: 	 2
//│ ║        	^^
//│ ╟── expression of type `1` is not a function
//│ ║  l.52: 	1
//│ ╙──      	^
//│ res: error

