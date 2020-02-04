package com.isteam.ulsumap.ui.send;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.isteam.ulsumap.R;

public class SendFragment extends Fragment {

    private Button send;
    private EditText name;
    private EditText text;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_send, container, false);

        send = (Button) root.findViewById(R.id.button_send);
        name = (EditText) root.findViewById(R.id.editText_name);
        text = (EditText) root.findViewById(R.id.editText_text);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent feedbackEmail = new Intent(Intent.ACTION_SEND);

                feedbackEmail.setType("text/email");
                feedbackEmail.putExtra(Intent.EXTRA_EMAIL, new String[] {"gusmax73@yandex.ru"});
                feedbackEmail.putExtra(Intent.EXTRA_SUBJECT, "Обратная связь.");
                feedbackEmail.putExtra(Intent.EXTRA_TEXT,"Здравствуйте, меня зовут " + name.getText() + ". \n" + text.getText());
                startActivity(Intent.createChooser(feedbackEmail, getString(R.string.feedback_hint)));
            }
        });


        return root;
    }
}