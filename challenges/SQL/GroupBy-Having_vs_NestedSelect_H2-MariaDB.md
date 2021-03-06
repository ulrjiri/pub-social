# Group-By vs. Nested Select

## Technical Constraints

 H2 Database, using MariaDB dialect.

Connection string:

```cmd
jdbc:h2:/.../h2/data/db1;MODE=MariaDB;
```

It may be needed to create a new database using the command line (Shell) tool:

```cmd
java -cp h2-*.jar org.h2.tools.Shell
```

To configure listen port, the content of ~/.h2.server.properties has to be adjusted:

```cmd
#H2 Server Properties
...
webAllowOthers=false
webPort=18082
```

URL in the browser might be adjusted to localhost in some cases:

[http://localhost:18082/](http://localhost:18082/)

## Input Data

```SQL
CREATE TABLE EMPL (ID INTEGER, DEPT_ID INTEGER, SALARY INTEGER);

INSERT INTO EMPL (ID, DEPT_ID, SALARY) VALUES (1, 1, 1001);
INSERT INTO EMPL (ID, DEPT_ID, SALARY) VALUES (2, 1, 1002);
INSERT INTO EMPL (ID, DEPT_ID, SALARY) VALUES (3, 1, 1003);
INSERT INTO EMPL (ID, DEPT_ID, SALARY) VALUES (4, 2, 1004);
INSERT INTO EMPL (ID, DEPT_ID, SALARY) VALUES (5, 2, 1005);
INSERT INTO EMPL (ID, DEPT_ID, SALARY) VALUES (6, 2, 106);
```

## Task

Find out whether all employees in a given department have salary > 1000.

DEPT_ID (ID of the given department) is the only input parameter.

Exactly one value true or false is expected in the output.

Solution should be simple SQL so that compatibility with multiple databases is ensured (PostgreSQL, MariaDB, Oracle, H2).

## Solutions

### Solution 1

Suboptimal solution using group-by and having. Does not work properly in negative cases.

```SQL
-- The following works just in positive case (DEPT_ID=1). 
-- In negative case (DEPT_ID=2), empty set (null) is returned.
SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END AS ALL_SALARIES_OVER
FROM EMPL E
WHERE DEPT_ID = __given_dept_id__
GROUP BY DEPT_ID
HAVING COUNT(*) = SUM(CASE WHEN E.SALARY > 1000 THEN 1 ELSE 0 END)
```

The "GROUP BY" is redundant in the above query because there is already a strict filtering for the DEPT_ID in the WHERE clause. The query can be simplified:

```SQL
-- The following works just in positive case (DEPT_ID=1). 
-- In negative case (DEPT_ID=2), empty set (null) is returned.
SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END AS ALL_SALARIES_OVER
FROM EMPL E
WHERE DEPT_ID = __given_dept_id__
HAVING COUNT(*) = SUM(CASE WHEN E.SALARY > 1000 THEN 1 ELSE 0 END)
```

### Solution 2

Better solution using nested select. Works in all cases.

```SQL
-- In positive case (DEPT_ID=1), true is returned.
-- In negative case (DEPT_ID=2), false is returned.
-- In case of non-existing DEPT_ID, false is returned.
SELECT CASE WHEN COUNT(*) > 0 THEN TRUE ELSE FALSE END AS ALL_SALARIES_OVER 
FROM (SELECT COUNT(*) AS COUNT_ALL, 
             SUM(CASE WHEN E.SALARY > 1000 THEN 1 ELSE 0 END) AS COUNT_SALARIES_OVER 
      FROM EMPL E
      WHERE DEPT_ID = __given_dept_id__) N
WHERE N.COUNT_ALL = N.COUNT_SALARIES_OVER
```

## Links

[http://www.h2database.com/html/tutorial.html](http://www.h2database.com/html/tutorial.html)

[http://www.h2database.com/html/grammar.html](http://www.h2database.com/html/grammar.html)
