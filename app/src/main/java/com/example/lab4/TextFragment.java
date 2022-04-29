package com.example.lab4;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.fragment.app.Fragment;

/**
 * gets data from activity, display it
 * changes it
 */
public class TextFragment extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String INPUT_TEXT = "text";
    private EditText editText;

    public TextFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment TextFragment.
     */
    public static TextFragment newInstance(String input) {
        TextFragment fragment = new TextFragment();
        Bundle args = new Bundle();
        args.putString(INPUT_TEXT, input);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_text, container, false);
        editText = rootView.findViewById(R.id.edit_text_fragment);

        assert getArguments() != null;
        editText.setText(String.valueOf(getArguments().getString(INPUT_TEXT)));

        return rootView;
    }

    public String getInputText() {
        return String.valueOf(editText.getText());
    }
}