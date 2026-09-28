# Code Explanation for Beginners

This walks through the project from zero. Read it in order — later sections assume you understood
the earlier ones.

## 0. The absolute basics you need first

- **Class**: a blueprint. `class Student { ... }` describes what every Student *has* (fields) and
  *can do* (methods). It's not a real student until you write `new Student(...)`.
- **Object**: an actual thing built from a class blueprint, sitting in memory. `new Student(...)`
  creates one.
- **Field**: a variable that belongs to an object (or class), e.g. `private String name;`.
- **Method**: a function that belongs to a class, e.g. `public void displayDetails() { ... }`.
- **`package`**: a folder-like namespace for organizing classes. The very first line of every file,
  e.g. `package com.exam.model;`, says "this class lives in this folder".
- **`import`**: lets one file use a class defined in a different package, e.g.
  `import com.exam.service.Reportable;` in `User.java`.
- **`public` / `private`**: who is allowed to see a field or method. `private` = only code inside
  this same class; `public` = anyone, anywhere.

---

## 1. The three enums (`UserRole.java`, `QuestionType.java`, `ExamStatus.java`)

```java
package com.exam.model;

public enum UserRole {
    STUDENT,
    INSTRUCTOR,
    ADMIN
}
```

An `enum` is a variable type with a **fixed** list of allowed values. Instead of using a `String`
for a role (where someone could accidentally type `"studnet"`), `UserRole` can only ever be
`STUDENT`, `INSTRUCTOR`, or `ADMIN` — the compiler enforces it. Later you'll see fields like
`private UserRole role;` and comparisons like `role == UserRole.STUDENT`.

---

## 2. `User.java` — the abstract base class

```java
public abstract class User implements Reportable {
```
- `abstract` means: you can never write `new User(...)` directly. It's incomplete on purpose —
  it exists only to be extended by `Student` and `Instructor`.
- `implements Reportable` means: this class promises to provide every method listed in the
  `Reportable` interface (here, just `generateReport()`).

```java
private int userId;
private String name;
private String username;
private String password;
private UserRole role;
private static int totalUsers = 0;
```
These are **fields**. All are `private` — this is **encapsulation**: the outside world can't
reach into a `User` and change `name` directly; it must go through a public method (`setName`).
`totalUsers` has the extra keyword `static`, which means it does **not** belong to one User — it
belongs to the whole `User` class, shared by every object. Every time a `User` is built, we do
`++totalUsers`, so this field is basically a running head-count of every user ever created.

```java
public User() {
    this("Unknown", "guest", "guest123", UserRole.STUDENT);
}

public User(String name, String username, String password, UserRole role) {
    this.userId = ++totalUsers;
    this.name = name;
    ...
}
```
These are **constructors** — special methods (same name as the class, no return type) that run
when you write `new User(...)`. There are two of them with different parameter lists: this is
**constructor overloading**. The first, empty one calls the second one using `this(...)` — this is
called **constructor chaining**, and it avoids repeating the same setup code twice.

Inside the second constructor, `this.name = name;` — here `this` refers to "the object currently
being built". The parameter is also called `name`, so without `this.` Java wouldn't know whether
you meant the field or the parameter. `this.name` unambiguously means "my field".

```java
public String getName() { return name; }
public void setName(String name) { this.name = name; }
```
This getter/setter pair is how outside code reads or changes `name`, even though the field itself
is `private`. This indirection is the whole point of encapsulation — later, you could add
validation inside `setName` (e.g. reject empty names) without changing any other file.

```java
public abstract void displayDetails();
```
No `{ }` body, and it's marked `abstract`. This is a **contract**: "every non-abstract subclass of
User MUST write its own `displayDetails()` method." `Student` and `Instructor` each do this
differently.

```java
@Override
public String toString() { ... }
```
Every Java object secretly already has a `toString()` method inherited from the built-in `Object`
class, which by default prints ugly things like `User@1a2b3c`. **Overriding** it means: "when
someone prints this object (e.g. `System.out.println(user)`), use MY formatted text instead."
`@Override` isn't required, but it's good practice — it tells the compiler "I intend to override
something", and the compiler will error out if I misspelled the method name.

```java
@Override
public boolean equals(Object obj) { ... }
```
By default, `==` and `.equals()` on objects compare *memory addresses* (are these two variables
pointing at the literal same object?). We override `equals()` so two `User` objects with the same
`userId` are treated as "the same user" even if they're technically different objects in memory.

---

## 3. `Student.java` and `Instructor.java` — inheritance

```java
public class Student extends User {
```
`extends User` means Student **inherits** everything from `User` (all its fields via getters, all
its methods) and can add its own. This is the "is-a" relationship: a Student *is a* User.

```java
public Student(String name, String username, String password) {
    super(name, username, password, UserRole.STUDENT);
    this.results = new ArrayList<>();
}
```
`super(...)` calls the **parent class's** constructor. Since `User`'s fields are `private`, Student
cannot set them directly — it must ask the parent to do it via `super(...)`. Java actually forces
every constructor to call some parent constructor first (even if you don't write it, an invisible
`super()` runs automatically).

```java
@Override
public void displayDetails() {
    System.out.printf("Student -> Name: %-15s Username: %-10s Exams Taken: %d%n",
            getName(), getUsername(), results.size());
}
```
This *fulfills* the abstract promise from `User`. `%-15s` inside `printf` means "print a String,
left-aligned, padded to 15 characters wide" — that's how the columns line up in the console output.

```java
@Override
public String generateReport() {
    return super.generateReport() + " | Role: STUDENT | Exams Taken: " + results.size();
}
```
Here `super.generateReport()` explicitly calls the **parent's version** of the method first, then
adds more text on top. This is different from `@Override` alone — `super.xxx()` lets you *reuse*
the parent's logic instead of throwing it away.

`Instructor.java` follows the exact same pattern with different fields/text.

---

## 4. Why `User`, `Student`, `Instructor` demonstrate "dynamic binding"

Look at this snippet from `Main.java`:

```java
User[] sampleUsers = {
        new Student("Demo Student", "demo", "demo123"),
        new Instructor("Demo Instructor", "demoinst", "demo123", "Mathematics")
};
for (User u : sampleUsers) {
    u.displayDetails();
}
```
The array is declared as `User[]`, but each *slot* actually holds a real `Student` or `Instructor`
object. When `u.displayDetails()` runs, Java looks at what `u` **actually is at that moment** (not
what the variable is *declared* as) and calls the matching overridden version. That decision
happens at runtime — hence "dynamic binding" / "runtime polymorphism". It's what lets you write
one loop that correctly handles many different subclasses.

---

## 5. `Question.java`, `MCQQuestion.java`, `DescriptiveQuestion.java` — a second hierarchy

This mirrors the User hierarchy but demonstrates an **interface** instead of only inheritance:

```java
public interface Evaluatable {
    double evaluate(String submittedAnswer);
}
```
An interface is like an abstract class that is *pure contract* — it (usually) has no fields and no
implemented methods, only method signatures. `MCQQuestion` and `DescriptiveQuestion` both
`implements Evaluatable` but grade answers in completely different ways:

- `MCQQuestion.evaluate()`: exact string match against the correct option letter.
- `DescriptiveQuestion.evaluate()`: counts how many expected keywords appear in the answer and
  gives partial credit proportional to the match.

In `ExamService.conductExam()`:
```java
Evaluatable evaluator = q;
double marksAwarded = evaluator.evaluate(answer);
```
`q` is declared as `Question`, but we store it in an `Evaluatable` variable. Whichever concrete
class `q` really is, `.evaluate(answer)` runs *that* class's version. The calling code never needs
an `if (q instanceof MCQQuestion)` check — that's the value interfaces give you.

---

## 6. `Exam.java` — encapsulation, `final`, static counters, method overloading

```java
public final class Exam implements Reportable {
```
`final` on a *class* means "nobody may ever `extends Exam`". We chose this because an Exam
represents a finalized, already-scheduled paper — letting someone silently create a
`SpecialExam extends Exam` that overrides grading behaviour would be a real-world integrity risk
(a mini-project version of "don't let students subclass the exam to cheat").

```java
public static final double PASS_PERCENTAGE = 40.0;
```
`static final` on a *field* is a true constant: `static` (one shared copy, not per-object) plus
`final` (cannot be reassigned after being set once). Constants are traditionally written in
UPPER_CASE.

```java
public void addQuestion(Question q) { questions.add(q); }
public void addQuestion(Question q, boolean announce) { ... }
```
Two methods, same name `addQuestion`, different parameter lists. Java picks the right one to run
based on how many/what type of arguments you pass at the call site — this is **method
overloading**. (Overloading ≠ overriding: overloading is about multiple methods in the *same*
class with different signatures; overriding is a *subclass* replacing a parent's method with the
*same* signature.)

---

## 7. `Result.java` — casting

```java
this.percentage = (totalMarks == 0) ? 0 : (scoredMarks / totalMarks) * 100;
this.finalScore = (int) this.percentage;
```
`percentage` is a `double` (a decimal number, e.g. `86.666...`). `finalScore` is declared as an
`int` (whole numbers only). You cannot assign a `double` to an `int` without saying so explicitly,
because information could be lost — Java forces you to write `(int)` in front to say "yes, I know,
truncate the decimal part on purpose." This is **explicit casting**. (`86.9` becomes `86`, not
`87` — casting `double` to `int` always truncates, it doesn't round.)

---

## 8. `ExamService.java` — the "service" layer

This class doesn't represent a real-world thing (unlike Student or Exam) — it exists purely to
hold the application's logic and its collections of objects (`List<Student> students`, etc.). This
is a common pattern: keep your **data classes** (model) separate from your **behaviour/orchestration
classes** (service). It's also why the project has two packages: `com.exam.model` for the "nouns"
and `com.exam.service` for the "verbs".

```java
for (int i = 0; i < questions.size(); i++) {
    Question q = questions.get(i);
    q.display();
    ...
    String answer = sc.nextLine().trim();
    if (answer.equalsIgnoreCase("skip")) {
        continue;
    }
    ...
}
```
- `for (int i = 0; ...)` is a classic counting loop: start `i` at 0, keep going while the condition
  is true, add 1 to `i` after each pass.
- `sc.nextLine()` reads one full line of console input as a `String`. `.trim()` removes leading and
  trailing whitespace the user might have accidentally typed.
- `.equalsIgnoreCase("skip")` compares two Strings for equality without caring about upper/lower
  case (`"SKIP"`, `"Skip"`, `"skip"` all match). Never use `==` to compare Strings for content in
  Java — `==` checks if it's literally the same object in memory, not the same text.
- `continue;` immediately jumps to the next loop iteration, skipping the grading code below it for
  this one question — useful for a "skip this question" feature.

---

## 9. `Main.java` — tying it all together

```java
Scanner sc = new Scanner(System.in);
```
`Scanner` reads from `System.in`, which is the keyboard (standard input). Every `sc.nextLine()`
call pauses the program until the user presses Enter.

```java
while (running) {
    printMenu();
    int choice = readInt(sc);
    switch (choice) {
        case 1: registerStudentFlow(sc, service); break;
        case 2: takeExamFlow(sc, service); break;
        ...
        case 5: running = false; break;
        default: System.out.println("Invalid choice, please select 1-5.");
    }
}
```
- `while (running)` keeps looping as long as the `boolean running` is `true`.
- `switch (choice)` jumps straight to the matching `case` instead of writing a long chain of
  `if / else if`. Each `case` ends with `break;`, which stops it from "falling through" into the
  next case. `default` catches any value that didn't match a case.
- Setting `running = false;` inside `case 5` doesn't stop the loop *immediately* — it lets the
  current loop pass finish, then the `while` condition is checked again and this time it's false,
  so the loop exits.

```java
int precedenceResult = 10 + 20 * 2 - 9 % 4;
```
Java evaluates `*` and `%` before `+` and `-` (just like normal maths: multiplication/division/
modulo bind tighter than addition/subtraction). So this is really
`10 + (20 * 2) - (9 % 4)` = `10 + 40 - 1` = `49`. `%` is the "remainder" operator: `9 % 4` is the
remainder after dividing 9 by 4, which is `1`.

```java
String[] skills = csvSkills.split(",");
```
`.split(",")` breaks one String into an array of Strings, cutting at every comma. So
`"Java,OOP,Collections"` becomes `["Java", "OOP", "Collections"]` — very handy for parsing simple
input.

---

## 10. Glossary — quick reference

| Term | One-line meaning |
|---|---|
| Class | Blueprint for objects |
| Object | A real instance built from a class (`new Something()`) |
| Field | A variable belonging to an object/class |
| Method | A function belonging to a class |
| Constructor | Special method that runs when an object is created |
| Encapsulation | Hiding fields as `private`, exposing controlled access via getters/setters |
| Inheritance | A class (`extends`) reusing and specializing another class |
| `super` | Refers to the parent class — used to call its constructor/methods |
| `this` | Refers to the current object — used to resolve naming clashes or chain constructors |
| Overloading | Same method/constructor name, different parameter lists, same class |
| Overriding | Subclass replaces a parent method with the *same* signature |
| Abstract class | A class that can't be instantiated directly; may contain unfinished (`abstract`) methods |
| Interface | A pure contract of method signatures a class promises to implement |
| Polymorphism / dynamic binding | Calling an overridden method via a parent-type or interface-type reference, and having the *actual* object's version run at runtime |
| `static` | Belongs to the class itself, shared by all objects, not any one instance |
| `final` | Cannot be changed (field/variable) or extended (class) or overridden (method) |
| Casting | Explicitly converting one data type into another, e.g. `(int) someDouble` |
| Enum | A type with a fixed, named set of possible values |
