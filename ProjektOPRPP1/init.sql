DROP TABLE IF EXISTS headphonesTechnology
DROP TABLE IF EXISTS HeadphonesUse
DROP TABLE IF EXISTS Headphones
DROP TABLE IF EXISTS Technology
DROP TABLE IF EXISTS UsePurpose
DROP TABLE IF EXISTS Brand
DROP TABLE IF EXISTS Type
DROP TABLE IF EXISTS Users
GO
-- ako nisu ovim redoslijedom obrisane, nece radit zog foreign keyjeva

--jel treba rucno postojat komanda za stvorit bazu

create table Brand(
ID integer primary key identity(1,1),
Name nvarchar(300) not null,
Description nvarchar(1000) null
);


create table Type(
ID integer primary key identity(1,1),
Name nvarchar(100) not null
);


create table Technology(
ID integer primary key identity(1,1),
Name nvarchar(300) not null,
Description nvarchar(1000) null
);


create table UsePurpose(
ID integer primary key identity(1,1),
Name nvarchar(100) not null
);


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


create table HeadphonesTechnology(
primary key(HeadphonesID,TechnologyID),
HeadphonesID int references Headphones(ID) on delete cascade,
TechnologyID int references Technology(ID)
);

create table HeadphonesUse(
primary key(HeadphonesID,UseID),
HeadphonesID int references Headphones(ID) on delete cascade,
UseID int references UsePurpose(ID)
);

go
create table Users(
ID int primary key identity(1,1),
Username nvarchar(150) unique not null,
Password nvarchar(100) not null,
Role nvarchar(50) not null,
ImagePath nvarchar(150) null
)

go
create proc GetAllBrands
as
begin
	select  b.ID,b.Name,b.Description
	from Brand as b
end

go
create proc GetBrandById 
@id int 
as
begin
	select b.ID,b.Name,b.Description
	from Brand as b
	where b.ID=@id
end

go
create proc InsertBrand
@Name nvarchar(100), @Description nvarchar(500)
as
begin
	insert into Brand(Name,Description)
		values(@Name,@Description)
end	

go
create proc UpdateBrand
@id int, @name nvarchar(100), @description nvarchar(500)
as
begin
	update Brand
	set 
		Name=@name,
		Description=@description
	where Brand.ID=@id
end

go
create proc DeleteBrand
@id int 
as
begin
	delete from Brand
	where Brand.ID = @id
end	

go
create proc GetAllTypes
as
begin
	select t.id, t.Name
	from Type as t
end

go
create proc GetTypeById
@id int
as
begin
	select t.id, t.Name
	from Type as t
	where t.ID=@id
end

go
create proc UpdateType
@id int , @name nvarchar(50)
as
begin
	update Type
	set 
		Name=@name
	where Type.ID=@id
end

go
create proc InsertType
@name nvarchar(50)
as
begin 
	insert into Type(Name)
		values(@name)
end

go
create proc DeleteType
@id int 
as
begin
	delete from Type
	where Type.ID=@id
end

go
create proc GetAllTechnologies
as
begin
	select t.ID,t.Name,t.Description
	from Technology as t
end	

go
create proc GetTechnologyById
@id int
as
begin
	select t.ID,t.Name,t.Description
	from Technology as t
	where t.ID=@id
end	

go
create proc InsertTechnology
@name nvarchar(100), @description nvarchar(500)
as
begin
	insert into Technology(Name,Description)
		values(@name,@description)
end	

go
create proc UpdateTechnology
@id int, @name nvarchar(100), @description nvarchar(500)
as
begin
	update Technology
	set
		Name=@name,
		Description=@description
	where Technology.ID=@id
end	

go
create proc RemoveTechnology
@id int
as
begin
	delete from Technology
	where Technology.ID=@id
end	

go
create proc GetAllUses
as
begin
	select f.ID,f.Name from UsePurpose as f
end

go
create proc GetUseByID
@id int
as
begin
	select f.ID,f.Name from UsePurpose as f
	where f.ID=@id
end

go
create proc InsertUse
@name nvarchar(100)
as
begin
	insert into UsePurpose(Name)values(@name)
end

go
create proc UpdateUse
@id int,
@name nvarchar(100)
as
begin
	update UsePurpose 
	set Name=@name
	where UsePurpose.ID=@id
end

go
create proc DeleteUse
@id int 
as
begin
	delete from UsePurpose
	where UsePurpose.ID=@id
end

go
create proc GetAllHeadphones
as
begin
 select f.ID,f.Name,f.YearEdition,f.Impedance,f.MinFrequency,f.MaxFrequency,f.BatteryLife,f.Price,f.Description,f.ImagePath,f.BrandID,f.TypeID
 from Headphones as f
end

go
create proc GetHeadphonesByID
@id int 
as
begin
	select f.ID,f.Name,f.YearEdition,f.Impedance,f.MinFrequency,f.MaxFrequency,f.BatteryLife,f.Price,f.Description,f.ImagePath,f.BrandID,f.TypeID
	from Headphones as f
	where f.ID=@id
end

go
create proc GetHeadphonesByType
@typeID int
as
begin
	select f.ID,f.Name,f.YearEdition,f.Impedance,f.MinFrequency,f.MaxFrequency,f.BatteryLife,f.Price,f.Description,f.ImagePath,f.BrandID,f.TypeID
	from Headphones as f
	where f.TypeID=@typeID
end	

go
create proc GetHeadphonesByBrand
@brandID int
as
begin
	select f.ID,f.Name,f.YearEdition,f.Impedance,f.MinFrequency,f.MaxFrequency,f.BatteryLife,f.Price,f.Description,f.ImagePath,f.BrandID,f.TypeID
	from Headphones as f
	where f.BrandID=@brandID
end	

go
create or alter proc GetHeadphonesByName
@name nvarchar(50)
as
	select f.ID,f.Name,f.YearEdition,f.Impedance,f.MinFrequency,f.MaxFrequency,f.BatteryLife,f.Price,f.Description,f.ImagePath,f.BrandID,f.TypeID
	from Headphones as f
	where f.Name=@name

go
create proc GetHeadphonesByUse
@useID int
as
begin
	select f.ID,f.Name,f.YearEdition,f.Impedance,f.MinFrequency,f.MaxFrequency,f.BatteryLife,f.Price,f.Description,f.ImagePath,f.BrandID,f.TypeID
	from Headphones as f
	inner join HeadphonesUse as hu on hu.HeadphonesID=f.ID
	where hu.UseID=@useID
end	

go
create proc GetHeadphonesByMaxPrice
@maxPrice money
as
begin
	select f.ID,f.Name,f.YearEdition,f.Impedance,f.MinFrequency,f.MaxFrequency,f.BatteryLife,f.Price,f.Description,f.ImagePath,f.BrandID,f.TypeID
	from Headphones as f
	where f.BrandID<=@maxPrice
end	

go
create or alter proc InsertHeadphones
@name nvarchar(200), @yearEdition int, @impedance int, @minFreq int, @maxFreq int, @batteryLife int,@price money, @description nvarchar(1000), @imagePath nvarchar(500), @brandId int, @typeID int
as
begin
	insert into Headphones(Name, YearEdition,Impedance,MinFrequency,MaxFrequency,BatteryLife,Price,Description,ImagePath,BrandID,TypeID)
		values(@name,@yearEdition,@impedance,@minFreq,@maxFreq,@batteryLife, @price,@description,@imagePath, @brandId, @typeID)

		select SCOPE_IDENTITY() as generatedID
end

go
create proc UpdateHeadphones
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
end

go
create proc DeleteHeadphones 
@id int 
as
begin
	delete from Headphones
	where Headphones.ID=@id
end

go
create proc GetTechnologiesForHeadphones
@headphonesId int
as
begin
	select t.ID, t.Name,t.Description
	from Technology as t
	inner join headphonesTechnology as ht on ht.TechnologyID=t.ID
	where ht.HeadphonesID=@headphonesId
end

go
create proc GetUsagesForHeadphones
@headphonesID int
as
begin
	select up.ID, up.Name
	from UsePurpose as up
	inner join HeadphonesUse as hu on hu.UseID=up.ID
end	

go
create proc AddHeadphonesTechnology
@technologyID int,
@headphonesID int
as
begin
	insert into headphonesTechnology(TechnologyID,HeadphonesID)
		values(@technologyID,@headphonesID)
end	

go
create proc RemoveHeadphonesTechnology
@technologyID int,
@headphonesID int
as
begin
	delete from headphonesTechnology 
	where TechnologyID=@technologyID and HeadphonesID=@headphonesID
end	

go
create or alter proc AddHeadphonesUse
@useId int,
@headphonesID int
as
begin
	insert into HeadphonesUse (UseID,HeadphonesID)
		values(@useId, @headphonesID)
end	

go
create proc RemoveHeadphonesUse
@useId int,
@headphonesID int 
as
begin
	delete from HeadphonesUse 
	where UseID=@useId and HeadphonesID=@headphonesID
end	

go
create or alter proc GetAllUsers
as
begin
	select u.id, u.Username, u.Password, u.Role, u.ImagePath
	from Users as u
end

go
create or alter proc GetUserById
@id int 
as
begin
	select u.id, u.Username, u.Password, u.Role, u.ImagePath
	from Users as u
	where u.ID=@id
end

go
create or alter proc GetUserByUsername
@username nvarchar(100)
as
begin
	select u.ID,u.Username,u.Password,u.Role, u.ImagePath
	from Users as u 
	where u.Username=@username
end

go
create or alter proc InsertUser
@name nvarchar(100), @password nvarchar(50), @role nvarchar(20), @imagePath nvarchar(150)
as
begin
	insert into Users(Username,Password,Role, ImagePath)
		values(@name, @password, @role, @imagePath)
end

go
create or alter proc UpdateUser
@id int,@name nvarchar(100), @password nvarchar(50), @role nvarchar(20), @imagePath nvarchar(150)
as
begin
	update Users
	set		
		Username=@name,
		Password=@password,
		Role=@role,
		ImagePath=@imagePath
	where Users.ID=@id
end

go
create or alter proc DeleteUser
@id int
as
begin
	delete from Users
	where Users.ID=@id
end


insert into Users(Username,Password,Role)
	values('admin', 'nimda','ADMIN')
