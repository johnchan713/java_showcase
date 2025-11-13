package com.example.demo.showcase;

import java.lang.annotation.*;
import java.lang.reflect.*;
import java.util.Arrays;

/**
 * Demonstrates Java Reflection API
 * Covers class inspection, method/field access, annotations, dynamic invocation, and more
 */
public class ReflectionShowcase {

    public static void demonstrate() {
        System.out.println("\n========== REFLECTION SHOWCASE ==========\n");

        classInspectionDemo();
        fieldAccessDemo();
        methodInvocationDemo();
        constructorDemo();
        annotationDemo();
        genericReflectionDemo();
        arrayReflectionDemo();
        modifierDemo();
        proxyDemo();
    }

    // ========== Class Inspection ==========

    private static void classInspectionDemo() {
        System.out.println("--- Class Inspection ---");
        System.out.println("Ways to obtain Class object and inspect it\n");

        // Ways to get Class object
        System.out.println("1. Ways to get Class object:");
        Class<String> class1 = String.class;
        Class<?> class2 = "Hello".getClass();
        Class<?> class3 = null;
        try {
            class3 = Class.forName("java.lang.String");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("  String.class: " + class1);
        System.out.println("  \"Hello\".getClass(): " + class2);
        System.out.println("  Class.forName(\"java.lang.String\"): " + class3);

        // Class information
        System.out.println("\n2. Class information for ArrayList:");
        Class<?> listClass = java.util.ArrayList.class;
        System.out.println("  Name: " + listClass.getName());
        System.out.println("  Simple name: " + listClass.getSimpleName());
        System.out.println("  Canonical name: " + listClass.getCanonicalName());
        System.out.println("  Package: " + listClass.getPackage().getName());
        System.out.println("  Modifiers: " + Modifier.toString(listClass.getModifiers()));
        System.out.println("  Is interface: " + listClass.isInterface());
        System.out.println("  Is abstract: " + Modifier.isAbstract(listClass.getModifiers()));
        System.out.println("  Is enum: " + listClass.isEnum());
        System.out.println("  Is array: " + listClass.isArray());

        // Superclass and interfaces
        System.out.println("\n3. Hierarchy:");
        System.out.println("  Superclass: " + listClass.getSuperclass().getSimpleName());
        System.out.println("  Interfaces: ");
        for (Class<?> iface : listClass.getInterfaces()) {
            System.out.println("    - " + iface.getSimpleName());
        }

        System.out.println();
    }

    // ========== Field Access ==========

    private static void fieldAccessDemo() {
        System.out.println("--- Field Access and Modification ---");
        System.out.println("Accessing and modifying fields via reflection\n");

        Person person = new Person("Alice", 30);
        Class<?> personClass = person.getClass();

        System.out.println("1. Get all fields:");
        Field[] fields = personClass.getDeclaredFields();
        for (Field field : fields) {
            System.out.println("  " + Modifier.toString(field.getModifiers()) + " " +
                             field.getType().getSimpleName() + " " + field.getName());
        }

        // Access private field
        System.out.println("\n2. Access private field:");
        try {
            Field nameField = personClass.getDeclaredField("name");
            System.out.println("  Field: " + nameField.getName());
            System.out.println("  Is accessible: " + nameField.canAccess(person));

            nameField.setAccessible(true); // Bypass private access
            String name = (String) nameField.get(person);
            System.out.println("  Current value: " + name);

            // Modify private field
            nameField.set(person, "Bob");
            System.out.println("  Modified to: " + nameField.get(person));
            System.out.println("  Verify via getter: " + person.getName());

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        // Field types
        System.out.println("\n3. Field type information:");
        try {
            Field nameField = personClass.getDeclaredField("name");
            System.out.println("  Type: " + nameField.getType());
            System.out.println("  Generic type: " + nameField.getGenericType());
            System.out.println("  Is primitive: " + nameField.getType().isPrimitive());
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        System.out.println();
    }

    // ========== Method Invocation ==========

    private static void methodInvocationDemo() {
        System.out.println("--- Method Invocation ---");
        System.out.println("Dynamically invoke methods via reflection\n");

        Person person = new Person("Charlie", 25);
        Class<?> personClass = person.getClass();

        // Get all methods
        System.out.println("1. Public methods:");
        Method[] methods = personClass.getDeclaredMethods();
        for (Method method : methods) {
            System.out.println("  " + method.getName() + "(" +
                             Arrays.toString(method.getParameterTypes()) + "): " +
                             method.getReturnType().getSimpleName());
        }

        // Invoke getter
        System.out.println("\n2. Invoke getter method:");
        try {
            Method getNameMethod = personClass.getMethod("getName");
            String name = (String) getNameMethod.invoke(person);
            System.out.println("  getName() returned: " + name);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        // Invoke setter
        System.out.println("\n3. Invoke setter method:");
        try {
            Method setAgeMethod = personClass.getMethod("setAge", int.class);
            setAgeMethod.invoke(person, 35);
            System.out.println("  setAge(35) called");
            System.out.println("  Verify: age = " + person.getAge());
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        // Invoke private method
        System.out.println("\n4. Invoke private method:");
        try {
            Method privateMethod = personClass.getDeclaredMethod("privateMethod");
            privateMethod.setAccessible(true);
            String result = (String) privateMethod.invoke(person);
            System.out.println("  privateMethod() returned: " + result);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        // Method with parameters
        System.out.println("\n5. Method with multiple parameters:");
        try {
            Method greetMethod = personClass.getMethod("greet", String.class);
            String greeting = (String) greetMethod.invoke(person, "World");
            System.out.println("  greet(\"World\") returned: " + greeting);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        System.out.println();
    }

    // ========== Constructor Reflection ==========

    private static void constructorDemo() {
        System.out.println("--- Constructor Reflection ---");
        System.out.println("Create instances dynamically via reflection\n");

        Class<?> personClass = Person.class;

        // Get constructors
        System.out.println("1. Available constructors:");
        Constructor<?>[] constructors = personClass.getConstructors();
        for (Constructor<?> constructor : constructors) {
            System.out.println("  " + constructor.getName() + "(" +
                             Arrays.toString(constructor.getParameterTypes()) + ")");
        }

        // Create instance with default constructor
        System.out.println("\n2. Create instance with no-arg constructor:");
        try {
            Constructor<?> noArgConstructor = personClass.getConstructor();
            Person person1 = (Person) noArgConstructor.newInstance();
            System.out.println("  Created: " + person1);
        } catch (NoSuchMethodException | InstantiationException |
                 IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        // Create instance with parameterized constructor
        System.out.println("\n3. Create instance with parameterized constructor:");
        try {
            Constructor<?> paramConstructor = personClass.getConstructor(String.class, int.class);
            Person person2 = (Person) paramConstructor.newInstance("David", 40);
            System.out.println("  Created: " + person2);
        } catch (NoSuchMethodException | InstantiationException |
                 IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        // Alternative: Class.newInstance() (deprecated but shown for completeness)
        System.out.println("\n4. Using Class.getDeclaredConstructor().newInstance():");
        try {
            Person person3 = personClass.getDeclaredConstructor().newInstance();
            System.out.println("  Created: " + person3);
        } catch (InstantiationException | IllegalAccessException |
                 InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }

        System.out.println();
    }

    // ========== Annotation Reflection ==========

    private static void annotationDemo() {
        System.out.println("--- Annotation Reflection ---");
        System.out.println("Read annotations at runtime\n");

        Class<?> personClass = Person.class;

        // Class annotations
        System.out.println("1. Class annotations:");
        if (personClass.isAnnotationPresent(Entity.class)) {
            Entity entity = personClass.getAnnotation(Entity.class);
            System.out.println("  @Entity(name=\"" + entity.name() + "\")");
        }

        // Field annotations
        System.out.println("\n2. Field annotations:");
        try {
            Field nameField = personClass.getDeclaredField("name");
            if (nameField.isAnnotationPresent(Column.class)) {
                Column column = nameField.getAnnotation(Column.class);
                System.out.println("  name field:");
                System.out.println("    @Column(name=\"" + column.name() + "\", nullable=" + column.nullable() + ")");
            }

            Field ageField = personClass.getDeclaredField("age");
            if (ageField.isAnnotationPresent(Column.class)) {
                Column column = ageField.getAnnotation(Column.class);
                System.out.println("  age field:");
                System.out.println("    @Column(name=\"" + column.name() + "\", nullable=" + column.nullable() + ")");
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        // Method annotations
        System.out.println("\n3. Method annotations:");
        try {
            Method greetMethod = personClass.getMethod("greet", String.class);
            if (greetMethod.isAnnotationPresent(Deprecated.class)) {
                System.out.println("  greet() is @Deprecated");
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        // All annotations
        System.out.println("\n4. All class annotations:");
        Annotation[] annotations = personClass.getAnnotations();
        for (Annotation annotation : annotations) {
            System.out.println("  " + annotation);
        }

        System.out.println();
    }

    // ========== Generic Type Reflection ==========

    private static void genericReflectionDemo() {
        System.out.println("--- Generic Type Reflection ---");
        System.out.println("Inspect generic type information\n");

        try {
            // Generic field
            Field listField = GenericExample.class.getDeclaredField("stringList");
            Type genericType = listField.getGenericType();

            System.out.println("1. Generic field type:");
            System.out.println("  Field: stringList");
            System.out.println("  Type: " + listField.getType());
            System.out.println("  Generic type: " + genericType);

            if (genericType instanceof ParameterizedType) {
                ParameterizedType paramType = (ParameterizedType) genericType;
                System.out.println("  Raw type: " + paramType.getRawType());
                System.out.println("  Actual type arguments: " + Arrays.toString(paramType.getActualTypeArguments()));
            }

            // Generic method
            Method genericMethod = GenericExample.class.getMethod("process", Object.class);
            System.out.println("\n2. Generic method:");
            System.out.println("  Method: " + genericMethod.getName());
            TypeVariable<?>[] typeParams = genericMethod.getTypeParameters();
            System.out.println("  Type parameters: " + Arrays.toString(typeParams));
            System.out.println("  Return type: " + genericMethod.getGenericReturnType());

        } catch (NoSuchFieldException | NoSuchMethodException e) {
            e.printStackTrace();
        }

        System.out.println();
    }

    // ========== Array Reflection ==========

    private static void arrayReflectionDemo() {
        System.out.println("--- Array Reflection ---");
        System.out.println("Create and manipulate arrays dynamically\n");

        // Create array
        System.out.println("1. Create array via reflection:");
        Object intArray = Array.newInstance(int.class, 5);
        System.out.println("  Created: int[5]");
        System.out.println("  Length: " + Array.getLength(intArray));

        // Set values
        System.out.println("\n2. Set array values:");
        for (int i = 0; i < 5; i++) {
            Array.set(intArray, i, i * 10);
        }
        System.out.print("  Values: [");
        for (int i = 0; i < Array.getLength(intArray); i++) {
            System.out.print(Array.get(intArray, i));
            if (i < Array.getLength(intArray) - 1) System.out.print(", ");
        }
        System.out.println("]");

        // Multi-dimensional array
        System.out.println("\n3. Create 2D array:");
        Object twoDArray = Array.newInstance(int.class, 3, 3);
        System.out.println("  Created: int[3][3]");
        System.out.println("  Component type: " + twoDArray.getClass().getComponentType());

        // Array type checking
        System.out.println("\n4. Array type checking:");
        Class<?> arrayClass = intArray.getClass();
        System.out.println("  Is array: " + arrayClass.isArray());
        System.out.println("  Component type: " + arrayClass.getComponentType());

        System.out.println();
    }

    // ========== Modifier Information ==========

    private static void modifierDemo() {
        System.out.println("--- Modifier Information ---");
        System.out.println("Inspect access modifiers and other flags\n");

        Class<?> personClass = Person.class;

        System.out.println("1. Class modifiers:");
        int classModifiers = personClass.getModifiers();
        System.out.println("  Modifiers: " + Modifier.toString(classModifiers));
        System.out.println("  Is public: " + Modifier.isPublic(classModifiers));
        System.out.println("  Is final: " + Modifier.isFinal(classModifiers));
        System.out.println("  Is abstract: " + Modifier.isAbstract(classModifiers));

        System.out.println("\n2. Field modifiers:");
        Field[] fields = personClass.getDeclaredFields();
        for (Field field : fields) {
            int modifiers = field.getModifiers();
            System.out.println("  " + field.getName() + ": " + Modifier.toString(modifiers));
            System.out.println("    Private: " + Modifier.isPrivate(modifiers));
            System.out.println("    Static: " + Modifier.isStatic(modifiers));
            System.out.println("    Final: " + Modifier.isFinal(modifiers));
        }

        System.out.println();
    }

    // ========== Dynamic Proxy ==========

    private static void proxyDemo() {
        System.out.println("--- Dynamic Proxy ---");
        System.out.println("Create proxy instances at runtime\n");

        // Create proxy
        System.out.println("1. Create proxy for Calculator interface:");
        Calculator calculator = (Calculator) Proxy.newProxyInstance(
            Calculator.class.getClassLoader(),
            new Class<?>[]{Calculator.class},
            (proxy, method, args) -> {
                System.out.println("  [PROXY] Method called: " + method.getName());
                System.out.println("  [PROXY] Arguments: " + Arrays.toString(args));

                // Actual logic
                if (method.getName().equals("add")) {
                    int result = (int) args[0] + (int) args[1];
                    System.out.println("  [PROXY] Result: " + result);
                    return result;
                } else if (method.getName().equals("multiply")) {
                    int result = (int) args[0] * (int) args[1];
                    System.out.println("  [PROXY] Result: " + result);
                    return result;
                }
                return null;
            }
        );

        // Use proxy
        System.out.println("\n2. Using proxy:");
        int sum = calculator.add(5, 3);
        System.out.println("  Final result from add: " + sum);

        int product = calculator.multiply(4, 7);
        System.out.println("  Final result from multiply: " + product);

        // Proxy information
        System.out.println("\n3. Proxy information:");
        System.out.println("  Is proxy: " + Proxy.isProxyClass(calculator.getClass()));
        System.out.println("  Proxy class: " + calculator.getClass().getName());
        System.out.println("  Interfaces: " + Arrays.toString(calculator.getClass().getInterfaces()));

        System.out.println("\nProxy use cases:");
        System.out.println("  ✓ Logging and debugging");
        System.out.println("  ✓ Security and access control");
        System.out.println("  ✓ Lazy loading");
        System.out.println("  ✓ AOP (Aspect-Oriented Programming)");
        System.out.println("  ✓ RPC/Remote method invocation");

        System.out.println();
    }

    // ========== Sample Classes for Reflection ==========

    @Entity(name = "person_table")
    static class Person {
        @Column(name = "person_name", nullable = false)
        private String name;

        @Column(name = "person_age", nullable = true)
        private int age;

        public Person() {
            this.name = "Unknown";
            this.age = 0;
        }

        public Person(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        @Deprecated
        public String greet(String target) {
            return "Hello, " + target + "! I'm " + name;
        }

        private String privateMethod() {
            return "This is a private method";
        }

        @Override
        public String toString() {
            return "Person{name='" + name + "', age=" + age + "}";
        }
    }

    static class GenericExample<T> {
        private java.util.List<String> stringList;
        private T genericField;

        public <E> E process(E item) {
            return item;
        }
    }

    interface Calculator {
        int add(int a, int b);
        int multiply(int a, int b);
    }

    // Custom annotations
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface Entity {
        String name();
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @interface Column {
        String name();
        boolean nullable() default true;
    }
}
