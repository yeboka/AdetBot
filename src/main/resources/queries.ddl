create table users(
    chatId int primary key unique ,
    userName varchar(50) unique not null,
    firstName varchar(50),
    lastName varchar(50),
    language_ varchar(10)
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


drop table activeHabits;
drop table completedHabits;
drop table users;


