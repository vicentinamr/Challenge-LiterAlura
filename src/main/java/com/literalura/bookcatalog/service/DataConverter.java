package com.literalura.bookcatalog.service;

public interface DataConverter {

    public <T> T obterDados(String json, Class<T> classe);
}
