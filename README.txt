## Seam Carving for Content Aware Image Resizing
** Specifications:**https://www.coursera.org/learn/algorithms-part2/programming/cOdkz/seam-carving
** Optimization Tips **
* When finding a seam, call energy() at most once per pixel. For example, you can save the energies in a local variable energy[][] and access the information directly from the 2D array (instead of recomputing from scratch).
* Avoid redundant calls to the get() method in Picture. For example, to access the red, green, and blue components of a pixel, call get() only once (and not three times).

#### ASSESSMENT SUMMARY

Compilation:  PASSED
API:          PASSED

Spotbugs:     PASSED
PMD:          FAILED (2 warnings)
Checkstyle:   PASSED

Correctness:  31/31 tests passed
Memory:       6/6 tests passed
Timing:       17/17 tests passed

Aggregate score: 100.00%
[Compilation: 5%, API: 5%, Spotbugs: 0%, PMD: 0%, Checkstyle: 0%, Correctness: 60%, Memory: 10%, Timing: 20%]
