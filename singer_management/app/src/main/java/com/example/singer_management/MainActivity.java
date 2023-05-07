package com.example.singer_management;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    // 위젯 변수 선언
    myDBHelper myHelper;
    EditText edtName, edtNumber, edtNameResult, edtNumberResult;
    Button btnInit, btnInsert, btnSelect;
    SQLiteDatabase sqlDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 제목 설정
        setTitle("가수 그룹 관리 db");

        // 위젯에 접근하기위한 객체 선언
        edtName = (EditText) findViewById(R.id.edtName);
        edtNumber = (EditText) findViewById(R.id.edtNumber);
        edtNameResult = (EditText) findViewById(R.id.etdNameResult);
        edtNumberResult = (EditText) findViewById(R.id.edtNumberResult);
        btnInit = (Button) findViewById(R.id.btnInit);
        btnInsert = (Button) findViewById(R.id.btnInsert);
        btnSelect = (Button) findViewById(R.id.btnSelect);

        // myDBHelper 인스턴스 생성 > 이 때 myDBHelper()의 생성자가 실행 되면서 groupDB 파일이 생성된다.
        myHelper = new myDBHelper(this);
        // 초기화 버튼 클릭
        btnInit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sqlDB = myHelper.getWritableDatabase();                     // groupDB를 write 전용 db로 열기
                myHelper.onUpgrade(sqlDB, 1, 2);      // onUpgrade() 메서드 호출
                sqlDB.close();
            }
        });

        // 입력 버튼 클릭
        btnInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sqlDB = myHelper.getWritableDatabase();                     // groupDB를 write 전용 db로 열기
                sqlDB.execSQL( "INSERT INTO groupTBL VALUES ( '" + edtName.getText().toString() + "', " + edtNumber.getText().toString()+");");     // INSERT QUERY 작성
                sqlDB.close();
                Toast.makeText(getApplicationContext(),"입력됨", 0).show();            // 입력이 성공하면 "입력됨" 이라는 토스트 메시지가 뜸.
            }
        });

        // 조회 버튼 클릭
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sqlDB = myHelper.getReadableDatabase();                     // groupDB를 Read 전용 db로 열기
                Cursor cursor;                                              // Cursor 객체 생성
                cursor = sqlDB.rawQuery("SELECT * FROM groupTBL;", null);   // 모든 테이블을 조회한 후 커서 대입, 현재는 커서가 첫번째 행을 가리키고 있음

                String strNames = "그룹 이름" + "\r\n" + "_______" + "\r\n";
                String strNumbers = "인원" + "\r\n" + "_______" + "\r\n";

                // 행 데이터의 개수만큼 반복, moveToNext() 메서드는 커서 변수의 다음 행으로 넘어간다. 만약 다음행이 없다면 false를 반환하고 while문 종료
                while (cursor.moveToNext())
                {
                    strNames += cursor.getString(0) + "\r\n";       // 현재 커서의 열 번호 데이터 값을 반환하여 문자열 변수에 누적한다. 0은 0번째 column(그룹 이름 column) , 1은 1번째 column(인원 column)
                    strNumbers += cursor.getString(1) + "\r\n";
                }

                // 누적한 문자열 출력
                edtNameResult.setText(strNames);
                edtNumberResult.setText(strNumbers);

                cursor.close();
                sqlDB.close();
            }
        });



    }

    // SQLiteOpenHelper 클래스를 상속받는 myDBHelper 클래스를 정의
    public class myDBHelper extends SQLiteOpenHelper{
        // 생성자 정의, "groupDB"는 새로 생성될 db의 이름, 1 : db version 처음에는 1로 지정
        public myDBHelper(Context context) {
            super(context, "groupDB", null, 1);
        }

        // 추상 메서드 오버라이딩 - onCreate()
        @Override
        public void onCreate(SQLiteDatabase db) {
            // 테이블 생성 쿼리 작성
            db.execSQL( "CREATE TABLE groupTBL (gName CHAR(20) PRIMARY KEY, gNumber INTEGER); ");
        }

        // 추상 메서드 오버라이딩 - onUpgrade
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // 테이블 삭제 후 다시 생성하는 쿼리 (초기화 할 때 호출)
            db.execSQL( "DROP TABLE IF EXISTS groupTBL " );
            onCreate(db);

        }
    }
}