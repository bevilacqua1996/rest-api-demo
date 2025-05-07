package com.jug.demo.templates;

public abstract class ReportGenerator<T> {

    private final T id;
    private final T repository;
    private T dataProcessed;

    public ReportGenerator(T id, T repository) {
        this.id = id;
        this.repository = repository;
    }

    public final String generate() {
        T data = loadData();
        process(data);
        return export();
    }

    protected abstract T loadData();
    protected abstract void process(T data);
    protected abstract String export();
}