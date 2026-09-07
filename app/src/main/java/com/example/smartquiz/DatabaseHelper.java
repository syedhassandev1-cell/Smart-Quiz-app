package com.example.smartquiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * DatabaseHelper for SQLite operations.
 * Demonstrates SQLiteOpenHelper, SQLiteDatabase, Cursor, ContentValues,
 * and complete CRUD (Create, Read, Update, Delete) operations.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    // Database information constants
    private static final String DATABASE_NAME = "smart_quiz.db";
    private static final int DATABASE_VERSION = 1;

    // Categories table constants
    public static final String TABLE_CATEGORIES = "categories";
    public static final String COLUMN_CAT_ID = "id";
    public static final String COLUMN_CAT_NAME = "name";
    public static final String COLUMN_CAT_DESC = "description";

    // Questions table constants
    public static final String TABLE_QUESTIONS = "questions";
    public static final String COLUMN_Q_ID = "id";
    public static final String COLUMN_Q_CATEGORY_ID = "category_id";
    public static final String COLUMN_Q_QUESTION = "question";
    public static final String COLUMN_Q_OPTION1 = "option1";
    public static final String COLUMN_Q_OPTION2 = "option2";
    public static final String COLUMN_Q_OPTION3 = "option3";
    public static final String COLUMN_Q_OPTION4 = "option4";
    public static final String COLUMN_Q_CORRECT = "correct_answer";

    // Quiz history table constants
    public static final String TABLE_HISTORY = "quiz_history";
    public static final String COLUMN_H_ID = "id";
    public static final String COLUMN_H_USER_NAME = "user_name";
    public static final String COLUMN_H_CATEGORY = "category";
    public static final String COLUMN_H_SCORE = "score";
    public static final String COLUMN_H_TOTAL = "total_questions";
    public static final String COLUMN_H_PERCENTAGE = "percentage";
    public static final String COLUMN_H_PERFORMANCE = "performance";
    public static final String COLUMN_H_DATE = "date";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL statement to create categories table
        String createCategoriesTable = "CREATE TABLE " + TABLE_CATEGORIES + " (" +
                COLUMN_CAT_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_CAT_NAME + " TEXT NOT NULL, " +
                COLUMN_CAT_DESC + " TEXT);";

        // SQL statement to create questions table
        String createQuestionsTable = "CREATE TABLE " + TABLE_QUESTIONS + " (" +
                COLUMN_Q_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_Q_CATEGORY_ID + " INTEGER, " +
                COLUMN_Q_QUESTION + " TEXT NOT NULL, " +
                COLUMN_Q_OPTION1 + " TEXT NOT NULL, " +
                COLUMN_Q_OPTION2 + " TEXT NOT NULL, " +
                COLUMN_Q_OPTION3 + " TEXT NOT NULL, " +
                COLUMN_Q_OPTION4 + " TEXT NOT NULL, " +
                COLUMN_Q_CORRECT + " INTEGER NOT NULL);";

        // SQL statement to create quiz history table
        String createHistoryTable = "CREATE TABLE " + TABLE_HISTORY + " (" +
                COLUMN_H_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_H_USER_NAME + " TEXT, " +
                COLUMN_H_CATEGORY + " TEXT, " +
                COLUMN_H_SCORE + " INTEGER, " +
                COLUMN_H_TOTAL + " INTEGER, " +
                COLUMN_H_PERCENTAGE + " REAL, " +
                COLUMN_H_PERFORMANCE + " TEXT, " +
                COLUMN_H_DATE + " TEXT);";

        db.execSQL(createCategoriesTable);
        db.execSQL(createQuestionsTable);
        db.execSQL(createHistoryTable);

        // Prepopulate SQLite with categories and questions
        seedDefaultData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop existing tables on schema update
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORIES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_HISTORY);
        onCreate(db);
    }

    /**
     * Seeds predefined categories and at least 30 multiple choice questions
     * across General Knowledge, Computer Science, and Mixed Quiz.
     */
    private void seedDefaultData(SQLiteDatabase db) {
        // 1. Insert Categories
        ContentValues cat1 = new ContentValues();
        cat1.put(COLUMN_CAT_NAME, "General Knowledge");
        cat1.put(COLUMN_CAT_DESC, "Test your understanding of global facts, history, science, and world trivia.");
        long catId1 = db.insert(TABLE_CATEGORIES, null, cat1);

        ContentValues cat2 = new ContentValues();
        cat2.put(COLUMN_CAT_NAME, "Computer Science");
        cat2.put(COLUMN_CAT_DESC, "Programming, Databases, OS, Data Structures, and Android Mobile Development.");
        long catId2 = db.insert(TABLE_CATEGORIES, null, cat2);

        ContentValues cat3 = new ContentValues();
        cat3.put(COLUMN_CAT_NAME, "Mixed Quiz");
        cat3.put(COLUMN_CAT_DESC, "A comprehensive blend of computer technology, logic, and general knowledge.");
        long catId3 = db.insert(TABLE_CATEGORIES, null, cat3);

        // 2. Insert 10 General Knowledge Questions
        insertQuestionInternal(db, (int) catId1, "What is the capital city of Australia?", "Sydney", "Melbourne", "Canberra", "Perth", 3);
        insertQuestionInternal(db, (int) catId1, "Which planet in our solar system is known as the Red Planet?", "Venus", "Mars", "Jupiter", "Saturn", 2);
        insertQuestionInternal(db, (int) catId1, "What is the chemical symbol for the element Gold?", "Au", "Ag", "Fe", "Gd", 1);
        insertQuestionInternal(db, (int) catId1, "Who is known for developing the Theory of Relativity?", "Isaac Newton", "Albert Einstein", "Nikola Tesla", "Galileo Galilei", 2);
        insertQuestionInternal(db, (int) catId1, "Which ocean is the largest by surface area on Earth?", "Atlantic Ocean", "Indian Ocean", "Arctic Ocean", "Pacific Ocean", 4);
        insertQuestionInternal(db, (int) catId1, "In which country can the ancient monument of Machu Picchu be found?", "Peru", "Chile", "Brazil", "Mexico", 1);
        insertQuestionInternal(db, (int) catId1, "What is the hardest natural substance known to humans?", "Titanium", "Diamond", "Graphene", "Quartz", 2);
        insertQuestionInternal(db, (int) catId1, "How many continents are there on planet Earth?", "5", "6", "7", "8", 3);
        insertQuestionInternal(db, (int) catId1, "What is the primary gas found in Earth's atmosphere?", "Oxygen", "Nitrogen", "Carbon Dioxide", "Hydrogen", 2);
        insertQuestionInternal(db, (int) catId1, "Which instrument is used to measure atmospheric air pressure?", "Thermometer", "Barometer", "Hygrometer", "Anemometer", 2);

        // 3. Insert 10 Computer Science Questions
        insertQuestionInternal(db, (int) catId2, "What does the abbreviation SQL stand for?", "Structured Query Language", "Sequential Question Link", "Simple Query Logic", "Standard Quick Layout", 1);
        insertQuestionInternal(db, (int) catId2, "In Android development, which class is used to manage SQLite database creation and versions?", "SQLiteDatabase", "SQLiteOpenHelper", "CursorAdapter", "DatabaseManager", 2);
        insertQuestionInternal(db, (int) catId2, "Which data structure follows the First-In, First-Out (FIFO) principle?", "Stack", "Tree", "Queue", "Graph", 3);
        insertQuestionInternal(db, (int) catId2, "In Java, which keyword is used to inherit a class?", "implements", "inherits", "extends", "abstract", 3);
        insertQuestionInternal(db, (int) catId2, "Which Android component represents a single screen with a user interface?", "Service", "BroadcastReceiver", "ContentProvider", "Activity", 4);
        insertQuestionInternal(db, (int) catId2, "What is the time complexity of binary search on a sorted array of N elements?", "O(1)", "O(n)", "O(log n)", "O(n^2)", 3);
        insertQuestionInternal(db, (int) catId2, "Which HTTP status code signifies a successful REST API request?", "200 OK", "301 Redirect", "404 Not Found", "500 Internal Error", 1);
        insertQuestionInternal(db, (int) catId2, "What does CPU stand for in computer hardware architecture?", "Central Processing Unit", "Control Program Utility", "Central Power Unit", "Core Performance Unit", 1);
        insertQuestionInternal(db, (int) catId2, "In relational databases, what guarantees the uniqueness of a record in a table?", "Foreign Key", "Primary Key", "Index Indexer", "Candidate Trigger", 2);
        insertQuestionInternal(db, (int) catId2, "Which Android mechanism is used to pass data between two Activities?", "SharedCursor", "Intent with Extras", "GlobalStaticState", "BroadcastReceiver", 2);

        // 4. Insert 10 Mixed Quiz Questions
        insertQuestionInternal(db, (int) catId3, "Who is credited with inventing the World Wide Web in 1989?", "Bill Gates", "Tim Berners-Lee", "Steve Jobs", "Alan Turing", 2);
        insertQuestionInternal(db, (int) catId3, "What is the smallest unit of digital information in computing?", "Byte", "Nibble", "Bit", "Word", 3);
        insertQuestionInternal(db, (int) catId3, "Which programming language was developed by James Gosling at Sun Microsystems?", "Python", "C++", "Java", "Ruby", 3);
        insertQuestionInternal(db, (int) catId3, "What does RAM stand for in computer systems?", "Random Access Memory", "Read Access Module", "Rapid Action Memory", "Routing Array Mechanism", 1);
        insertQuestionInternal(db, (int) catId3, "In which year was the first Android commercial phone (HTC Dream) released?", "2005", "2008", "2010", "2012", 2);
        insertQuestionInternal(db, (int) catId3, "Which protocol is primarily used to securely browse web pages over the internet?", "FTP", "HTTP", "HTTPS", "SMTP", 3);
        insertQuestionInternal(db, (int) catId3, "Which logic gate outputs true only if both input signals are true?", "OR Gate", "XOR Gate", "AND Gate", "NOT Gate", 3);
        insertQuestionInternal(db, (int) catId3, "What is the hexadecimal equivalent of decimal number 15?", "E", "F", "10", "A", 2);
        insertQuestionInternal(db, (int) catId3, "Which company originally created the Android operating system before Google acquired it?", "Motorola", "Android Inc.", "Symbian Corp", "Palm", 2);
        insertQuestionInternal(db, (int) catId3, "In Java OOP, wrapping data and methods into a single unit is called?", "Polymorphism", "Encapsulation", "Abstraction", "Inheritance", 2);
    }

    private void insertQuestionInternal(SQLiteDatabase db, int categoryId, String q, String opt1, String opt2, String opt3, String opt4, int correct) {
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_Q_CATEGORY_ID, categoryId);
        cv.put(COLUMN_Q_QUESTION, q);
        cv.put(COLUMN_Q_OPTION1, opt1);
        cv.put(COLUMN_Q_OPTION2, opt2);
        cv.put(COLUMN_Q_OPTION3, opt3);
        cv.put(COLUMN_Q_OPTION4, opt4);
        cv.put(COLUMN_Q_CORRECT, correct);
        db.insert(TABLE_QUESTIONS, null, cv);
    }

    // =========================================================================
    // CRUD: CREATE (INSERT)
    // =========================================================================

    /**
     * INSERT a completed quiz attempt into quiz_history table.
     */
    public long insertQuizHistory(QuizHistory history) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_H_USER_NAME, history.getUserName());
        values.put(COLUMN_H_CATEGORY, history.getCategory());
        values.put(COLUMN_H_SCORE, history.getScore());
        values.put(COLUMN_H_TOTAL, history.getTotalQuestions());
        values.put(COLUMN_H_PERCENTAGE, history.getPercentage());
        values.put(COLUMN_H_PERFORMANCE, history.getPerformance());
        values.put(COLUMN_H_DATE, history.getDate());

        long insertedId = db.insert(TABLE_HISTORY, null, values);
        return insertedId;
    }

    /**
     * INSERT a new question into questions table.
     */
    public long insertQuestion(Question question) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_Q_CATEGORY_ID, question.getCategoryId());
        cv.put(COLUMN_Q_QUESTION, question.getQuestion());
        cv.put(COLUMN_Q_OPTION1, question.getOption1());
        cv.put(COLUMN_Q_OPTION2, question.getOption2());
        cv.put(COLUMN_Q_OPTION3, question.getOption3());
        cv.put(COLUMN_Q_OPTION4, question.getOption4());
        cv.put(COLUMN_Q_CORRECT, question.getCorrectAnswer());

        return db.insert(TABLE_QUESTIONS, null, cv);
    }

    // =========================================================================
    // CRUD: READ (SELECT / RETRIEVE)
    // =========================================================================

    /**
     * SELECT all categories from categories table.
     */
    public List<Category> getAllCategories() {
        List<Category> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CATEGORIES, null, null, null, null, null, COLUMN_CAT_ID + " ASC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CAT_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CAT_NAME));
                String desc = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CAT_DESC));
                list.add(new Category(id, name, desc));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    /**
     * SELECT category by ID.
     */
    public Category getCategoryById(int categoryId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CATEGORIES, null, COLUMN_CAT_ID + "=?",
                new String[]{String.valueOf(categoryId)}, null, null, null);

        Category category = null;
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CAT_ID));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CAT_NAME));
            String desc = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CAT_DESC));
            category = new Category(id, name, desc);
            cursor.close();
        }
        return category;
    }

    /**
     * SELECT questions by category ID.
     */
    public List<Question> getQuestionsByCategory(int categoryId) {
        List<Question> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = (categoryId > 0) ? COLUMN_Q_CATEGORY_ID + "=?" : null;
        String[] selectionArgs = (categoryId > 0) ? new String[]{String.valueOf(categoryId)} : null;

        Cursor cursor = db.query(TABLE_QUESTIONS, null, selection, selectionArgs, null, null, COLUMN_Q_ID + " ASC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_Q_ID));
                int catId = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_Q_CATEGORY_ID));
                String questionText = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_Q_QUESTION));
                String opt1 = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_Q_OPTION1));
                String opt2 = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_Q_OPTION2));
                String opt3 = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_Q_OPTION3));
                String opt4 = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_Q_OPTION4));
                int correct = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_Q_CORRECT));

                list.add(new Question(id, catId, questionText, opt1, opt2, opt3, opt4, correct));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    /**
     * SELECT all questions from database.
     */
    public List<Question> getAllQuestions() {
        return getQuestionsByCategory(0);
    }

    /**
     * SELECT all quiz history records ordered by newest first.
     */
    public List<QuizHistory> getAllQuizHistory() {
        List<QuizHistory> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_HISTORY, null, null, null, null, null, COLUMN_H_ID + " DESC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_H_ID));
                String user = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_H_USER_NAME));
                String cat = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_H_CATEGORY));
                int score = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_H_SCORE));
                int total = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_H_TOTAL));
                double perc = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_H_PERCENTAGE));
                String perf = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_H_PERFORMANCE));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_H_DATE));

                list.add(new QuizHistory(id, user, cat, score, total, perc, perf, date));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    /**
     * SELECT quiz history filtered by category.
     */
    public List<QuizHistory> getQuizHistoryByCategory(String category) {
        if (category == null || category.equalsIgnoreCase("All")) {
            return getAllQuizHistory();
        }
        List<QuizHistory> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_HISTORY, null, COLUMN_H_CATEGORY + "=?",
                new String[]{category}, null, null, COLUMN_H_ID + " DESC");

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_H_ID));
                String user = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_H_USER_NAME));
                String cat = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_H_CATEGORY));
                int score = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_H_SCORE));
                int total = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_H_TOTAL));
                double perc = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_H_PERCENTAGE));
                String perf = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_H_PERFORMANCE));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_H_DATE));

                list.add(new QuizHistory(id, user, cat, score, total, perc, perf, date));
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    // =========================================================================
    // STATISTICS CALCULATION (Using SQLite SELECT & Aggregation)
    // =========================================================================

    public int getTotalQuizzesCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_HISTORY, null);
        int count = 0;
        if (cursor != null && cursor.moveToFirst()) {
            count = cursor.getInt(0);
            cursor.close();
        }
        return count;
    }

    public int getBestScore() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MAX(" + COLUMN_H_SCORE + ") FROM " + TABLE_HISTORY, null);
        int best = 0;
        if (cursor != null && cursor.moveToFirst()) {
            best = cursor.getInt(0);
            cursor.close();
        }
        return best;
    }

    public double getAveragePercentage() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT AVG(" + COLUMN_H_PERCENTAGE + ") FROM " + TABLE_HISTORY, null);
        double avg = 0.0;
        if (cursor != null && cursor.moveToFirst()) {
            avg = cursor.getDouble(0);
            cursor.close();
        }
        return Math.round(avg * 10.0) / 10.0;
    }

    // =========================================================================
    // CRUD: UPDATE
    // =========================================================================

    /**
     * UPDATE an existing question's text, options, and correct answer.
     * Demonstrates SQLite UPDATE operation.
     */
    public int updateQuestion(Question question) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_Q_QUESTION, question.getQuestion());
        cv.put(COLUMN_Q_OPTION1, question.getOption1());
        cv.put(COLUMN_Q_OPTION2, question.getOption2());
        cv.put(COLUMN_Q_OPTION3, question.getOption3());
        cv.put(COLUMN_Q_OPTION4, question.getOption4());
        cv.put(COLUMN_Q_CORRECT, question.getCorrectAnswer());

        return db.update(TABLE_QUESTIONS, cv, COLUMN_Q_ID + "=?", new String[]{String.valueOf(question.getId())});
    }

    // =========================================================================
    // CRUD: DELETE
    // =========================================================================

    /**
     * DELETE an individual quiz history record by ID.
     */
    public int deleteHistoryById(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_HISTORY, COLUMN_H_ID + "=?", new String[]{String.valueOf(id)});
    }

    /**
     * DELETE all records from quiz history.
     */
    public int clearAllHistory() {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_HISTORY, null, null);
    }
}
