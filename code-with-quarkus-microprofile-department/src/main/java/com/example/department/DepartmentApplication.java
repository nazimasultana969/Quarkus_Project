package com.example.department;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class DepartmentApplication {

    public static void main(String[] args) {

        Quarkus.run(args);
    }
}