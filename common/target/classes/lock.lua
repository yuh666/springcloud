local key = KEYS[1]
local val = ARGV[1]
local currVal = redis.call("get", key)
local result = 0
if currVal == val then
    redis.call("del", key)
    result = 1
end
return result