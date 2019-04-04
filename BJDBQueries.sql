USE BlackJackDB;

-- Inserting new authenticated user
INSERT INTO Users (fullName, email, username, password, balance, bailoutTokens, score)
	VALUES ('full name', 'email', 'username', 'password', 500, 0, 500);
    
-- Authenticate Login
SELECT username, password FROM Users WHERE username = 'username';

-- Leaderboard (score = balance - 750 * bailout tokens, username, rank (USE ASC))
SELECT username, score FROM Users ORDER BY score ASC;

-- Add Balance (for when leaving the table)
UPDATE Users
	SET balance = (balance + INSERT_AMOUNT),
		score = (score + INSERT_AMOUNT)
	WHERE userID = 'INSERT_USERID';

-- Get Balance (checking before entering table)
SELECT balance FROM Users WHERE username = 'INSERT_USERID';

-- Get userID
SELECT userID FROM Users WHERE username = 'INSERT_USERNAME';

-- Remove Balance (for enterinUPDATE Users
UPDATE Users
	SET balance = (balance - INSERT_AMOUNT)
	WHERE userID = 'INSERT_USERID';
    
-- Get bailout tokens
SELECT bailoutTokens FROM Users WHERE username = 'INSERT_USERNAME';

-- Add bailout token
UPDATE Users
	SET balance = (balance + 500),
		score = (score - 750),
		bailoutTokens = (bailoutTokens + 1)
	WHERE userID = 'INSERT_USERID';
    
-- Cash out bailout (remove)
UPDATE Users
	SET balance = (balance - 500),
		score = (score + 750),
		bailoutTokens = (bailoutTokens - 1)
	WHERE userID = 'INSERT_USERID';
    
SELECT * FROM Users;