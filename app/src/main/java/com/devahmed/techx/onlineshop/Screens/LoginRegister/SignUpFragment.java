package com.devahmed.techx.onlineshop.Screens.LoginRegister;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.devahmed.techx.onlineshop.R;
import com.devahmed.techx.onlineshop.Utils.UtilsDialog;


public class SignUpFragment extends Fragment {
    static EditText phone;

    ProgressBar progressBar;
    TextView termsText;
    String terms = "الرجاء قراءة الشروط والأحكام بعناية.\n" +
            "\n" +
            " \n" +
            "\n" +
            "* إذا قمت بالتسجيل على تطبيق fox mart، سوف يُطلب منك توفير بعض المعلومات الأساسية حتى نستطيع أن نتحقق من الهوية \n" +
            "\n" +
            "* يتم الاحتفاظ بهذه المعلومات من قبلنا بشكل آمن، لخدمتك بشكل اسرع فى المرات القادمة.\n" +
            "\n" +
            "يرجى من مستخدمينا عدم استخدام التطبيق بشكل قد يؤثر على أداء أو سلامة أو أمن التطبيق  مثل:\n" +
            "\n" +
            "- استخراج أو نسخ قوائم أو معلومات أو بيانات منتجات.\n" +
            "\n" +
            "- استخراج أو نسخ محتوى التطبيق.\n" +
            "\n" +
            "- استخراج أو نسخ الكود الخاص بتطبيق fox mart.\n" +
            "\n" +
            "- استخراج أو نسخ قوائم أو معلومات أو بيانات.\n" +
            "\n" +
            "سياسة المنتجات و الأسعار\n" +
            "\n" +
            "تحصل فوكس مارت على أسعار المنتجات وغيرها من المعلومات الخاصة بهذه المنتجات من الشركات. بالإضافة إلى ذلك ، يستخدم التطبيق الخاص بنا العديد من الطرق للتحقق من دقة المعلومات المقدمة ، لكن التطبيق لا يضمن تمامًا أن تكون جميع المعلومات المقدمة دقيقة دائمًا.\n" +
            "\n" +
            "مسئوليتنا عن المحتوى\n" +
            "\n" +
            "- قد يحتوي الموقع على أخطاء فنية، أو أخطاء مطبعية.\n" +
            "\n" +
            "- لا يمكن للتطبيق أن يضمن بشكل كامل و مطلق أن كل المعلومات المقدمة ستكون دقيقة في اغلب الأوقات.\n" +
            "\n" +
            "\n" +
            "حظر لسوء الاستخدام\n" +
            "\n" +
            "يحتفظ التطبيق بالحق في حظر أو استبعاد أو إنهاء استخدام أي فرد أو كيان لخدمات التطبيق في حالة انتهاك شروط الاستخدام الخاصة به إذا لوحظ أي إنتهاك للشروط والأحكام الخاصة بالخدمة أو الإضرار بالخدمة المقدمة\n" +
            "\n" +
            "شروط الاستخدام\n" +
            "\n" +
            "- أي نزاع أو مطالبة تنشأ عن أو فيما يتعلق بهذا التطبيق يجب أن تخضع وتفسر وفقا لقوانين مصر.\n" +
            "\n" +
            "-يتم التعامل مع العميل الذي يقوم بالطلب وكثره الالغاء لحظر حسابه تماما توفيرا لوقت العملاء وسلامه وصول الطلب في الوقت المناسب \n" +
            "\n" +
            "- يُحظر على القاصرين التلاعب في البرنامج وسوء الاستخدام عن طريق طلب ثم الغاء";
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        phone = view.findViewById(R.id.phone_signup);
        progressBar = view.findViewById(R.id.addpost_progressbar);
        termsText = view.findViewById(R.id.termsText);
        termsText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UtilsDialog dialog = new UtilsDialog(context);
                dialog.showTextMessage(terms);
            }
        });
        return view;
    }


    public static String getUserPhone(){
        return phone.getText().toString();
    }

    public void setContext(Context context){
        this.context = context;
    }

}
