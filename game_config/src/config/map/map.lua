--[[Notice: This lua config file is auto generate by map.xls，don't modify it manually! --]]

local indexData = {
	key = 1, --key 
	cn = 2, --地图名称 
	fcn = 3, --地图key 
}

local data = {
	["1"] = {key="1",cn="主城",fcn="main",},
	["2"] = {key="2",cn="测试1",fcn="test1",},
}

local mt = {}
mt.__index = function(t,k)
	if indexData[k] then
		return rawget(t,indexData[k]) 
	end
	return
end
mt.__newindex = function(t,k,v)
	error('do not edit config')
end
mt.__metatable = false
for _,v in pairs(data) do
	setmetatable(v,mt)
end

return data