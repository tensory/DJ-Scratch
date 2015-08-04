package net.tensory.djscratch.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.codepath.oauth.OAuthLoginFragment;

import net.tensory.djscratch.R;
import net.tensory.djscratch.rest.TwitterClient;

public class LoginFragment extends OAuthLoginFragment<TwitterClient> {

    @Override
    public void onLoginSuccess() {
        onLoginResponseListener.onSuccess();
    }

    @Override
    public void onLoginFailure(Exception e) {
        onLoginResponseListener.onFailure(e);
    }

    /**
     * Simple callback interface for the Twitter login attempt.
     * Any activity that anticipates hosting this fragment must implement OnLoginResponseListener.
     */
    public interface OnLoginResponseListener {
        public void onSuccess();
        public void onFailure(Exception e);
    }

    private OnLoginResponseListener onLoginResponseListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        Button btnLogin = (Button) view.findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getClient().connect();
            }
        });
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            onLoginResponseListener = (OnLoginResponseListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnLoginResponseListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        onLoginResponseListener = null;
    }
}
