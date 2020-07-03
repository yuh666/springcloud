-- 要限流的KEY
local key = KEYS[1]

-- 用户还有几个token
local tokens_key = key .. "_token"
-- 用户上次上刷新token是什么时候
local timestamp_key = key .. "_ts"

-- token生成的速率
local rate = tonumber(ARGV[1])
-- 令牌桶的最大容量
local capacity = tonumber(ARGV[2])
-- 本次请求的时间
local now = tonumber(ARGV[3])
-- 本次请求需要几个令牌
local requested = tonumber(ARGV[4])

-- 令牌加满需要的时间
local fill_time = capacity/rate
-- 将过期时间设置为两次令牌加满的时间
-- 如果这么长时间就没来就过期来节省内存
local ttl = math.floor(fill_time*2)

-- 用户还剩几个token
local last_tokens = tonumber(redis.call("get", tokens_key))
if last_tokens == nil then
    -- 如果没有默认就是满桶
    last_tokens = capacity
end

-- 用户上次刷新token的时间
local last_refreshed = tonumber(redis.call("get", timestamp_key))
if last_refreshed == nil then
    -- 如果没有默认就是0
    last_refreshed = 0
end

-- 计算距离上次已经过了多长时间
local delta = math.max(0, now-last_refreshed)
-- 填充token
local filled_tokens = math.min(capacity, last_tokens+(delta*rate))
-- 本次是否够
local allowed = filled_tokens >= requested
local new_tokens = filled_tokens
-- 返回值 是否允许
local allowed_num = 0
if allowed then
    -- 可以通过 减掉本次的token数
    new_tokens = filled_tokens - requested
    -- 设置返回值
    allowed_num = 1
end

-- 更新token数量
redis.call("setex", tokens_key, ttl, new_tokens)
-- 更新刷新时间
redis.call("setex", timestamp_key, ttl, now)

-- 返回结果
return { allowed_num, new_tokens }
