"""
N = 3a + 5b
min(a + b) = ?

예를 들어, 18킬로그램 설탕을 배달해야 할 때, 3킬로그램 봉지 6개를 가져가도 되지만,
5킬로그램 3개와 3킬로그램 1개를 배달하면, 더 적은 개수의 봉지를 배달할 수 있다.

상근이가 설탕을 정확하게 N킬로그램 배달해야 할 때, 봉지 몇 개를 가져가면 되는지

첫째 줄에 N이 주어진다. (3 ≤ N ≤ 5000)
"""

N = int(input())

# N = 5a + 3b
# a + b = k

mink = None

for a in range(N):
    for b in range(N):
        if 5 * a + 3 * b == N:
            k = a + b
            if mink is None or k < mink:
                mink = k

print(mink or -1)