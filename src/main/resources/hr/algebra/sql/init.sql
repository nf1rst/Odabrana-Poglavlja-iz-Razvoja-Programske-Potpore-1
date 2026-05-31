use HeadphonesDB;
go

go

create table AdminActivities(
allowUsersToEdit bit not null
);
go

insert into AdminActivities([allowUsersToEdit]) values(0);
go

create table Brand(
ID integer primary key identity(1,1),
Name nvarchar(300) not null,
Description nvarchar(1000) null
);
go

create table Type(
ID integer primary key identity(1,1),
Name nvarchar(100) not null
);
go

create table Technology(
ID integer primary key identity(1,1),
Name nvarchar(300) not null,
Description nvarchar(1000) null
);
go

create table UsePurpose(
ID integer primary key identity(1,1),
Name nvarchar(100) not null
);
go

create table Headphones(
ID integer primary key identity(1,1),
Name nvarchar(300) not null,
YearEdition int,
Impedance int,
MinFrequency int,
MaxFrequency int,
BatteryLife int,
Price money not null,
Description nvarchar(1000),
ImagePath nvarchar(500),
BrandID int references Brand(ID),
TypeID int references Type(ID)
);
go

create table HeadphonesTechnology(
primary key(HeadphonesID,TechnologyID),
HeadphonesID int references Headphones(ID) on delete cascade,
TechnologyID int references Technology(ID)
);
go

create table HeadphonesUse(
primary key(HeadphonesID,UseID),
HeadphonesID int references Headphones(ID) on delete cascade,
UseID int references UsePurpose(ID)
);
go

create table Users(
ID int primary key identity(1,1),
Username nvarchar(150) unique not null,
Password nvarchar(60) not null,
Role nvarchar(50) not null,
ImagePath nvarchar(150) null
);
go

create or alter proc GetAllBrands
as
begin
	select b.ID, b.Name, b.Description
	from Brand as b
end;
go

create or alter proc GetBrandById 
@id int 
as
begin
	select b.ID, b.Name, b.Description
	from Brand as b
	where b.ID=@id
end;
go

create or alter proc InsertBrand
@Name nvarchar(100), @Description nvarchar(500)
as
begin
	insert into Brand(Name,Description)
		values(@Name,@Description)
end;
go

create or alter proc UpdateBrand
@id int, @name nvarchar(100), @description nvarchar(500)
as
begin
	update Brand
	set 
		Name=@name,
		Description=@description
	where Brand.ID=@id
end;
go

create or alter proc DeleteBrand
@id int 
as
begin
	delete from Brand
	where Brand.ID = @id
end;
go

create or alter proc GetAllTypes
as
begin
	select t.id, t.Name
	from Type as t
end;
go

create or alter proc GetTypeById
@id int
as
begin
	select t.id, t.Name
	from Type as t
	where t.ID=@id
end;
go

-- ISPRAVLJENO: Pisalo je "createor alter" spojeno
create or alter proc UpdateType
@id int , @name nvarchar(50)
as
begin
	update Type
	set 
		Name=@name
	where Type.ID=@id
end;
go

create or alter proc InsertType
@name nvarchar(50)
as
begin 
	insert into Type(Name)
		values(@name)
end;
go

create or alter proc DeleteType
@id int 
as
begin
	delete from Type
	where Type.ID=@id
end;
go

create or alter proc GetAllTechnologies
as
begin
	select t.ID,t.Name,t.Description
	from Technology as t
end;
go

create or alter proc GetTechnologyById
@id int
as
begin
	select t.ID,t.Name,t.Description
	from Technology as t
	where t.ID=@id
end;
go

create or alter proc InsertTechnology
@name nvarchar(100), @description nvarchar(500)
as
begin
	insert into Technology(Name,Description)
		values(@name,@description)
end;
go

create or alter proc UpdateTechnology
@id int, @name nvarchar(100), @description nvarchar(500)
as
begin
	update Technology
	set
		Name=@name,
		Description=@description
	where Technology.ID=@id
end;
go

create or alter proc RemoveTechnology
@id int
as
begin
	delete from Technology
	where Technology.ID=@id
end;
go

create or alter proc GetAllUses
as
begin
	select f.ID,f.Name from UsePurpose as f
end;
go

create or alter proc GetUseByID
@id int
as
begin
	select f.ID,f.Name from UsePurpose as f
	where f.ID=@id
end;
go

create or alter proc InsertUse
@name nvarchar(100)
as
begin
	insert into UsePurpose(Name) values(@name)
end;
go

create or alter proc UpdateUse
@id int,
@name nvarchar(100)
as
begin
	update UsePurpose 
	set Name=@name
	where UsePurpose.ID=@id
end;
go

create or alter proc DeleteUse
@id int 
as
begin
	delete from UsePurpose
	where UsePurpose.ID=@id
end;
go

create or alter proc GetAllHeadphones
as
begin
 select f.ID,f.Name,f.YearEdition,f.Impedance,f.MinFrequency,f.MaxFrequency,f.BatteryLife,f.Price,f.Description,f.ImagePath,f.BrandID,f.TypeID
 from Headphones as f
end;
go

create or alter proc GetHeadphonesByID
@id int 
as
begin
	select f.ID,f.Name,f.YearEdition,f.Impedance,f.MinFrequency,f.MaxFrequency,f.BatteryLife,f.Price,f.Description,f.ImagePath,f.BrandID,f.TypeID
	from Headphones as f
	where f.ID=@id
end;
go

create or alter proc GetHeadphonesByType
@typeID int
as
begin
	select f.ID,f.Name,f.YearEdition,f.Impedance,f.MinFrequency,f.MaxFrequency,f.BatteryLife,f.Price,f.Description,f.ImagePath,f.BrandID,f.TypeID
	from Headphones as f
	where f.TypeID=@typeID
end;	
go

create or alter proc GetHeadphonesByBrand
@brandID int
as
begin
	select f.ID,f.Name,f.YearEdition,f.Impedance,f.MinFrequency,f.MaxFrequency,f.BatteryLife,f.Price,f.Description,f.ImagePath,f.BrandID,f.TypeID
	from Headphones as f
	where f.BrandID=@brandID
end;	
go

create or alter proc GetHeadphonesByName
@name nvarchar(50)
as
begin
	select f.ID,f.Name,f.YearEdition,f.Impedance,f.MinFrequency,f.MaxFrequency,f.BatteryLife,f.Price,f.Description,f.ImagePath,f.BrandID,f.TypeID
	from Headphones as f
	where f.Name=@name
end;
go

create or alter proc GetHeadphonesByUse
@useID int
as
begin
	select f.ID,f.Name,f.YearEdition,f.Impedance,f.MinFrequency,f.MaxFrequency,f.BatteryLife,f.Price,f.Description,f.ImagePath,f.BrandID,f.TypeID
	from Headphones as f
	inner join HeadphonesUse as hu on hu.HeadphonesID=f.ID
	where hu.UseID=@useID
end;	
go

create or alter proc InsertHeadphones
@name nvarchar(200), @yearEdition int, @impedance int, @minFreq int, @maxFreq int, @batteryLife int,@price money, @description nvarchar(1000), @imagePath nvarchar(500), @brandId int, @typeID int
as
begin
	insert into Headphones(Name, YearEdition,Impedance,MinFrequency,MaxFrequency,BatteryLife,Price,Description,ImagePath,BrandID,TypeID)
		values(@name,@yearEdition,@impedance,@minFreq,@maxFreq,@batteryLife, @price,@description,@imagePath, @brandId, @typeID)

		select SCOPE_IDENTITY() as generatedID
end;
go

create or alter proc UpdateHeadphones
@id int,@name nvarchar(200), @yearEdition int, @impedance int, @minFreq int, @maxFreq int, @batteryLife int,@price money, @description nvarchar(1000), @imagePath nvarchar(500), @brandId int, @typeID int
as
begin
	update Headphones
	set 
		Name=@name,
		YearEdition=@yearEdition,
		Impedance=@impedance,
		MinFrequency=@minFreq,
		MaxFrequency=@maxFreq,
		BatteryLife=@batteryLife,
		Price=@price,
		Description=@description,
		ImagePath=@imagePath,
		BrandID=@brandId,
		TypeID=@typeID
	where Headphones.ID=@id
end;
go

create or alter proc DeleteHeadphones 
@id int 
as
begin
	delete from Headphones
	where Headphones.ID=@id
end;
go

create or alter proc GetTechnologiesForHeadphones
@headphonesId int
as
begin
	select t.ID, t.Name,t.Description
	from Technology as t
	inner join headphonesTechnology as ht on ht.TechnologyID=t.ID
	where ht.HeadphonesID=@headphonesId
end;
go

create or alter proc GetUsagesForHeadphones
@headphonesID int
as
begin
	select up.ID, up.Name
	from UsePurpose as up
	inner join HeadphonesUse as hu on hu.UseID=up.ID
    where hu.HeadphonesID=@headphonesID -- ISPRAVAK: dodana zaboravljena filtracija po ID-u slušalica
end;	
go

create or alter proc AddHeadphonesTechnology
@technologyID int,
@headphonesID int
as
begin
	insert into headphonesTechnology(TechnologyID,HeadphonesID)
		values(@technologyID,@headphonesID)
end;
go

-- ISPRAVAK: Pisalo je "create proc or alter" pogrešnim redoslijedom
create or alter proc RemoveHeadphonesTechnology
@technologyID int,
@headphonesID int
as
begin
	delete from headphonesTechnology 
	where TechnologyID=@technologyID and HeadphonesID=@headphonesID
end;	
go

create or alter proc AddHeadphonesUse
@useId int,
@headphonesID int
as
begin
	insert into HeadphonesUse (UseID,HeadphonesID)
		values(@useId, @headphonesID)
end;	
go

create or alter proc RemoveHeadphonesUse
@useId int,
@headphonesID int 
as
begin
	delete from HeadphonesUse 
	where UseID=@useId and HeadphonesID=@headphonesID
end;	
go

create or alter proc GetAllUsers
as
begin
	select u.id, u.Username, u.Password, u.Role, u.ImagePath
	from Users as u
end;
go

create or alter proc GetUserById
@id int 
as
begin
	select u.id, u.Username, u.Password, u.Role, u.ImagePath
	from Users as u
	where u.ID=@id
end;
go

create or alter proc GetUserByUsername
@username nvarchar(100)
as
begin
	select u.ID,u.Username,u.Password,u.Role, u.ImagePath
	from Users as u 
	where u.Username=@username
end;
go

create or alter proc InsertUser
@name nvarchar(100), @password nvarchar(60), @role nvarchar(20), @imagePath nvarchar(400)
as
begin
	insert into Users(Username,Password,Role, ImagePath)
		values(@name, @password, @role, @imagePath)
end;
go

create or alter proc UpdateUser
@id int,@name nvarchar(100), @password nvarchar(60), @role nvarchar(20), @imagePath nvarchar(150)
as
begin
	update Users
	set		
		Username=@name,
		Password=@password,
		Role=@role,
		ImagePath=@imagePath
	where Users.ID=@id
end;
go

create or alter proc DeleteUser
@id int
as
begin
	delete from Users
	where Users.ID=@id
end;
go

create or alter proc DeleteAllUsers
as
begin 
	delete
	from Users
	where Role='User'
end;
go

-- ISPRAVAK: @username parametar mora biti nvarchar(150), a ne int budući da je u tablici tekst!
create or alter proc DeleteUserByUsername
@username nvarchar(150)
as
begin
	delete
	from Users
	where Username=@username
end;
go

create or alter proc GetNumberOfEntries
as
begin
select
	(select COUNT(*) from Headphones) as headphonesNum,
	(select COUNT(*) from Brand) as brandNum,
	(select COUNT(*) from Technology) as techNum,
	(select COUNT(*) from UsePurpose) useNum,
	(select COUNT(*) from Type) as typeNum,
	(select COUNT(*) from Users) as usersNum
end;
go

create or alter proc DeleteAllEntities
as
begin 
	delete from HeadphonesTechnology
	delete from HeadphonesUse
	delete from Headphones
	delete from Brand
	delete from Technology
	delete from Type
	delete from UsePurpose
end; 
go

create or alter proc DeleteAllTables
as
begin 
	DROP TABLE IF EXISTS HeadphonesTechnology;
	DROP TABLE IF EXISTS HeadphonesUse;
	DROP TABLE IF EXISTS Headphones;
	DROP TABLE IF EXISTS Technology;
	DROP TABLE IF EXISTS UsePurpose;
	DROP TABLE IF EXISTS Brand;
	DROP TABLE IF EXISTS Type;
	DROP TABLE IF EXISTS Users;
	DROP TABLE IF EXISTS AdminActivities;
end; 
go


create or alter proc SetUserEditRights
@value bit
as
begin
	update AdminActivities
		set 
		allowUsersToEdit=@value
end;
go

create or alter proc GetUserEditRights
as
begin
	select allowUsersToEdit
	from AdminActivities
end;
go