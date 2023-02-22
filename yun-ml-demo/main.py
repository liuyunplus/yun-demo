import numpy as np
import matplotlib.pyplot as plt
from sklearn.preprocessing import StandardScaler

X = np.random.normal(100, 20, 100)
X = X.reshape(-1, 1)
plt.hist(X)
plt.show()

transfer = StandardScaler()
X_new = transfer.fit_transform(X)
plt.hist(X_new)
plt.show()