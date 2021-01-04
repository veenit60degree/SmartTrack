package com.json.parser;

public interface MainAsynListener<T> {

	public void onPostSuccess(T result, int id, boolean isSucess);

	public void onPostError(int id, int error);

}
