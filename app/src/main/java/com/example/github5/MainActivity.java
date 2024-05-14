package com.example.github5;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private ProgressBar mProgressBar;
    private TextView mTextView;
    private EditText et;
    private EditText et2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.textView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.INVISIBLE);
        et = findViewById(R.id.editText1);
        et2 = findViewById(R.id.editText2);
    }
    public void onClick1(View view) {
        GitHubService gitHubService = GitHubService.retrofit.create(GitHubService.class);
        final Call<List<Contributor>> call =
                gitHubService.repoContributors("square", "picasso");

        call.enqueue(new Callback<List<Contributor>>() {
            @Override
            public void onResponse(Call<List<Contributor>> call, Response<List<Contributor>> response) {
                final TextView textView = (TextView) findViewById(R.id.textView);
                textView.setText(response.body().toString());
                //final TextView textView2 = (TextView) findViewById(R.id.log);
                //textView2.setText(response.body().toString());
            }

            @Override
            public void onFailure(Call<List<Contributor>> call, Throwable throwable) {
                final TextView textView = (TextView) findViewById(R.id.textView);
                textView.setText("Что-то пошло не так: " + throwable.getMessage());
            }
        });
    }

    public void onClick2(View view) {
        mProgressBar.setVisibility(View.VISIBLE);

        mTextView = findViewById(R.id.textView);
        GitHubService2 gitHubService = GitHubService2.retrofit.create(GitHubService2.class);
        final Call<User> call = gitHubService.getUser(et.getText().toString());
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if(response.isSuccessful())
                {
                    User user = response.body();
                    mTextView.setText(
                            "Аккаунт Github: " + user.getName() +
                            "\nСайт: " + user.getBlog() +
                            "\nКомпания: " + user.getCompany()
                    );
                }
                else
                {
                    int statusCode = response.code();

                    ResponseBody errorBody = response.errorBody();
                    try {
                        mTextView.setText(errorBody.string());
                        mProgressBar.setVisibility(View.VISIBLE);
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                mTextView.setText("Что-то пошло не так" + t.getMessage());
            }
        });
    }

    public void onClick3(View view) {
        mProgressBar.setVisibility(View.VISIBLE);

        GitHubService3 gitHubService = GitHubService3.retrofit.create(GitHubService3.class);

        //final Call<List<Repos>> call = gitHubService.getRepos("alexanderklimov");
        final Call<List<Repos>> call = gitHubService.getRepos(et2.getText().toString());

        call.enqueue(new Callback<List<Repos>>() {
                         @Override
                         public void onResponse(Call<List<Repos>> call, Response<List<Repos>> response) {
                             //response.isSuccessfull() is true if the response code is 2xx
                             if (response.isSuccessful()) {
                                 // Выводим массив имён
                                 mTextView.setText(response.body().toString() + "\n");
                                 for (int i = 0; i < response.body().size(); i++) {
                                     // Выводим имена по отдельности
                                     mTextView.append(response.body().get(i).getName() + "\n");
                                 }

                                 mProgressBar.setVisibility(View.INVISIBLE);
                             } else {
                                 int statusCode = response.code();
                                 // Обрабатываем ошибкуalexander
                                 ResponseBody errorBody = response.errorBody();
                                 try {
                                     mTextView.setText(errorBody.string());
                                     mProgressBar.setVisibility(View.INVISIBLE);
                                 } catch (IOException e) {
                                     e.printStackTrace();
                                 }
                             }
                         }

                         @Override
                         public void onFailure(Call<List<Repos>> call, Throwable throwable) {
                             mTextView.setText("Что-то пошло не так: " + throwable.getMessage());
                         }
                     }
        );
    }

    public void onClick4(View view) {
        mProgressBar.setVisibility(View.VISIBLE);

        GitHubService4 gitHubService = GitHubService4.retrofit.create(GitHubService4.class);
        // часть слова
        final Call<GitResult> call =
                gitHubService.getUsers("alexanderklim");

        call.enqueue(new Callback<GitResult>() {
            @Override
            public void onResponse(Call<GitResult> call, Response<GitResult> response) {
                // response.isSuccessful() is true if the response code is 2xx
                if (response.isSuccessful()) {
                    GitResult result = response.body();

                    // Получаем json из github-сервера и конвертируем его в удобный вид
                    // Покажем только первого пользователя
                    String user = "Аккаунт Github: " + result.getItems().get(0).getLogin();
                    mTextView.setText(user);
                    Log.i("Git", String.valueOf(result.getItems().size()));

                    mProgressBar.setVisibility(View.INVISIBLE);
                } else {
                    int statusCode = response.code();

                    // handle request errors yourself
                    ResponseBody errorBody = response.errorBody();
                    try {
                        mTextView.setText(errorBody.string());
                        mProgressBar.setVisibility(View.INVISIBLE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<GitResult> call, Throwable throwable) {
                mTextView.setText("Что-то пошло не так: " + throwable.getMessage());
            }
        });
    }

}