package net.tensory.djscratch.rest;

/**
 * Interface defining success and failure callbacks, imposing a data type for success results.
 */
public interface Consumer<T> {
    public void onSuccess(T result);
    public void onFailure(int statusCode);
}
