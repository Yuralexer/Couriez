package com.yuralexer.couriez.fragments.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.yuralexer.couriez.R;
import com.yuralexer.couriez.activity.RegistrationActivity;

public class NameInputFragment extends Fragment {

    private TextInputLayout tilNameInput;
    private TextInputEditText etNameInput;
    private TextView tvTitle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_name_input, container, false);

        tilNameInput = view.findViewById(R.id.tilNameInput);
        etNameInput = view.findViewById(R.id.etNameInput);
        tvTitle = view.findViewById(R.id.text_view_title);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() instanceof RegistrationActivity) {
            String accountType = ((RegistrationActivity) getActivity()).getAccountTypeForFragments();
            if ("Физическое лицо".equals(accountType)) {
                tilNameInput.setHint("Введите ваше имя");
                tvTitle.setText("Как вас называть?");
            } else if ("Юридическое лицо".equals(accountType)) {
                tilNameInput.setHint("Введите название организации");
                tvTitle.setText("Как называется ваша организация?");
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        tilNameInput = null;
        etNameInput = null;
        tvTitle = null;
    }

    public boolean validateInput() {
        if (etNameInput == null || tilNameInput == null) return false;

        String name = etNameInput.getText() != null ? etNameInput.getText().toString().trim() : "";
        if (name.isEmpty()) {
            tilNameInput.setError("Поле не может быть пустым");
            return false;
        }

        tilNameInput.setError(null);
        return true;
    }

    public String getName() {
        return etNameInput.getText() != null ? etNameInput.getText().toString().trim() : "";
    }
}
