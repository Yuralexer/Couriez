package com.yuralexer.couriez.fragments.auth;

import android.os.Bundle;
import android.text.InputType;
import android.util.Pair;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.yuralexer.couriez.R;

import java.util.regex.Pattern;

public class ContactInfoFragment extends Fragment {

    private RadioGroup rgContactType;
    private RadioButton rbPhone;
    private RadioButton rbEmail;
    private TextInputLayout tilContactInput;
    private TextInputEditText etContactInput;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_info, container, false);

        rgContactType = view.findViewById(R.id.rgContactType);
        rbPhone = view.findViewById(R.id.rbPhone);
        rbEmail = view.findViewById(R.id.rbEmail);
        tilContactInput = view.findViewById(R.id.tilContactInput);
        etContactInput = view.findViewById(R.id.etContactInput);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        rgContactType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == rbPhone.getId()) {
                    tilContactInput.setHint("Введите номер телефона");
                    etContactInput.setInputType(InputType.TYPE_CLASS_PHONE);
                } else if (checkedId == rbEmail.getId()) {
                    tilContactInput.setHint("Введите адрес Email");
                    etContactInput.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                }
                if (etContactInput.getText() != null) {
                    etContactInput.getText().clear();
                }
                tilContactInput.setError(null);
            }
        });

        tilContactInput.setHint("Введите номер телефона");
        etContactInput.setInputType(InputType.TYPE_CLASS_PHONE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        rgContactType = null;
        rbPhone = null;
        rbEmail = null;
        tilContactInput = null;
        etContactInput = null;
    }

    public boolean validateInput() {
        if (etContactInput == null || tilContactInput == null) return false;

        String contactValue = etContactInput.getText() != null ? etContactInput.getText().toString().trim() : "";
        if (contactValue.isEmpty()) {
            tilContactInput.setError("Поле не может быть пустым");
            return false;
        }

        boolean isPhone = rgContactType.getCheckedRadioButtonId() == rbPhone.getId();
        if (isPhone) {
            if (!Pattern.matches("^\\+?[0-9]{10,15}$", contactValue)) {
                tilContactInput.setError("Некорректный номер телефона");
                return false;
            }
        } else {
            if (!Patterns.EMAIL_ADDRESS.matcher(contactValue).matches()) {
                tilContactInput.setError("Некорректный Email адрес");
                return false;
            }
        }

        tilContactInput.setError(null);
        return true;
    }

    public Pair<String, String> getContactInfo() {
        String type = (rgContactType.getCheckedRadioButtonId() == rbPhone.getId()) ? "Телефон" : "Email";
        String value = etContactInput.getText() != null ? etContactInput.getText().toString().trim() : "";
        return new Pair<>(type, value);
    }
}
