"""
1번 대각선 (↗, 홀) -> 1/1
2번 대각선 (↙, 짝) -> 1/2, 2/1
3번 대각선 (↗, 홀) -> 3/1, 2/2, 1/3
...
"""

X = int(input())

d = 0
high = 0
while high < X:
    d += 1
    high += d

i = high - X

if d % 2 == 0:
    x = 1 + i
    y = d - i
else:
    x = d - i
    y = 1 + i

print('{}/{}'.format(y, x))