INSERT INTO students (full_Name, furigana, nickname, email, region, age, gender)
VALUES ('田中太郎', 'タナカタロウ', 'タロウ', 'abc@example.com', '東京都', 25, '男性'),
       ('鈴木花子', 'スズキハナコ', 'ハナコ', 'def@example.com', '神奈川県', 19, '女性'),
       ('佐藤健一', 'サトウケンイチ', 'ケンイチ', 'ghi@example.com', '千葉県', 35, '男性'),
       ('高橋美咲', 'タカハシミサキ', 'ミサキ', 'jkl@example.com', '大阪府', 20, '女性'),
       ('伊藤大輔', 'イトウダイスケ', 'ダイスケ', 'mno@example.com', '北海道', 41, '男性');

INSERT INTO student_courses (student_id, course_name, course_start_at, course_end_at)
VALUES (1, 'Java', '2024-04-01', '2025-03-31'),
       (2, 'web', '2024-07-01', '2025-06-30'),
       (3, 'Python', '2023-04-01', '2024-03-31'),
       (3, 'java', '2024-04-01', '2025-03-31'),
       (4, 'Photoshop', '2025-04-01', '2026-03-31'),
       (5, 'Excel', '2024-04-01', '2025-03-31');
