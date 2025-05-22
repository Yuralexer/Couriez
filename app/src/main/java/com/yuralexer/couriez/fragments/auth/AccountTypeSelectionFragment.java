package com.yuralexer.couriez.fragments.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.yuralexer.couriez.R;

public class AccountTypeSelectionFragment extends Fragment {

    private RadioGroup rgAccountType;
    private RadioButton rbPhysicalPerson;
    private RadioButton rbLegalPerson;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account_type_selection, container, false);

        rgAccountType = view.findViewById(R.id.rgAccountType);
        rbPhysicalPerson = view.findViewById(R.id.rbPhysicalPerson);
        rbLegalPerson = view.findViewById(R.id.rbLegalPerson);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        rgAccountType = null;
        rbPhysicalPerson = null;
        rbLegalPerson = null;
    }

    public boolean validateInput() {
        if (rgAccountType == null) return false;
        return rgAccountType.getCheckedRadioButtonId() != -1;
    }

    public String getAccountType() {
        if (rgAccountType == null) return "";
        int checkedId = rgAccountType.getCheckedRadioButtonId();
        if (checkedId == rbPhysicalPerson.getId()) {
            return "Физическое лицо";
        } else if (checkedId == rbLegalPerson.getId()) {
            return "Юридическое лицо";
        }
        return "";
    }
}
