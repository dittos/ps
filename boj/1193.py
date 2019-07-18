"""
1 -> (0, 0) + (1, 0)
2 -> (1, 0) + (-1, 1)
3 -> (0, 1) + (0, 1)
4 -> (0, 2) + (1, -1)
5 -> (1, 1) + (1, -1)
6 -> (2, 0) + (1, 0)
7 -> (3, 0) + (-1, 1)
8 -> (2, 1)
...

→
↙ * 1
↓
↗ * 2
→
↙ * 3
↓
↗ * 4
...
"""

X = 10_000_000
X = int(input())

x = 1; y = 1
dx = 1; dy = 0
for i in range(X - 1):
    x += dx
    y += dy
    if dx == -1 and dy == 1:
        if x == 1:
            dx = 0
    elif dx == 1 and dy == -1:
        if y == 1:
            dy = 0
    elif dx == 1 and dy == 0:
        dx = -1; dy = 1
    elif dx == 0 and dy == 1:
        dx = 1; dy = -1

print('{}/{}'.format(y, x))