create table users(
    chatId int primary key unique ,
    userName varchar(50) unique not null,
    firstName varchar(50) not null,
    lastName varchar(50) not null
);
create table activeHabits(
    nameOfHabit varchar(50) not null,
    chatId int primary key unique,
    foreign key (chatId)
    references users(chatId),
    story varchar
);
create table completedHabits(
    nameOfHabit varchar(50) not null,
    chatId int primary key ,
    foreign key (chatId)
    references users(chatId),
    story varchar
);
