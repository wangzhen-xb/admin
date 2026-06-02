export * from './request'

export const debounce = <T extends (...args: any[]) => any>(fn: T, delay: number): T => {
  let timer: ReturnType<typeof setTimeout> | null = null
  return ((...args: any[]) => {
    if (timer) clearTimeout(timer)
    timer = setTimeout(() => fn(...args), delay)
  }) as T
}

export const throttle = <T extends (...args: any[]) => any>(fn: T, delay: number): T => {
  let lastTime = 0
  return ((...args: any[]) => {
    const now = Date.now()
    if (now - lastTime >= delay) {
      lastTime = now
      fn(...args)
    }
  }) as T
}

export const formatDate = (date: Date | string | number, format: string = 'YYYY-MM-DD HH:mm:ss'): string => {
  const d = new Date(date)
  const year = d.getFullYear()
  const month = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  const hours = String(d.getHours()).padStart(2, '0')
  const minutes = String(d.getMinutes()).padStart(2, '0')
  const seconds = String(d.getSeconds()).padStart(2, '0')

  return format
    .replace('YYYY', String(year))
    .replace('MM', month)
    .replace('DD', day)
    .replace('HH', hours)
    .replace('mm', minutes)
    .replace('ss', seconds)
}

export const getType = (value: any): string => {
  return Object.prototype.toString.call(value).slice(8, -1).toLowerCase()
}

export const isObject = (value: any): boolean => {
  return getType(value) === 'object' && value !== null
}

export const isArray = (value: any): boolean => {
  return Array.isArray(value)
}

export const isString = (value: any): boolean => {
  return typeof value === 'string'
}

export const isNumber = (value: any): boolean => {
  return typeof value === 'number' && isFinite(value)
}

export const isBoolean = (value: any): boolean => {
  return typeof value === 'boolean'
}

export const isFunction = (value: any): boolean => {
  return typeof value === 'function'
}

export const isUndefined = (value: any): boolean => {
  return typeof value === 'undefined'
}

export const isNull = (value: any): boolean => {
  return value === null
}

export const isEmpty = (value: any): boolean => {
  if (isUndefined(value) || isNull(value)) return true
  if (isString(value)) return value.trim() === ''
  if (isArray(value)) return value.length === 0
  if (isObject(value)) return Object.keys(value).length === 0
  return false
}

export const cloneDeep = <T>(value: T): T => {
  if (!isObject(value) && !isArray(value)) {
    return value
  }
  return JSON.parse(JSON.stringify(value))
}

export const merge = <T extends Record<string, any>>(target: T, ...sources: any[]): T => {
  const result = { ...target }
  for (const source of sources) {
    if (isObject(source)) {
      for (const key in source) {
        if (isObject(source[key]) && isObject(result[key])) {
          result[key] = merge(result[key], source[key])
        } else {
          result[key] = source[key]
        }
      }
    }
  }
  return result
}

export const pick = <T extends Record<string, any>, K extends keyof T>(obj: T, keys: K[]): Pick<T, K> => {
  const result = {} as Pick<T, K>
  for (const key of keys) {
    if (key in obj) {
      result[key] = obj[key]
    }
  }
  return result
}

export const omit = <T extends Record<string, any>, K extends keyof T>(obj: T, keys: K[]): Omit<T, K> => {
  const result = { ...obj } as Omit<T, K>
  for (const key of keys) {
    delete result[key as any]
  }
  return result
}

export const generateId = (): string => {
  return `${Date.now()}-${Math.random().toString(36).substr(2, 9)}`
}

export const trim = (str: string): string => {
  return str.trim()
}

export const capitalize = (str: string): string => {
  if (!str) return str
  return str.charAt(0).toUpperCase() + str.slice(1)
}

export const camelCase = (str: string): string => {
  return str.replace(/-([a-z])/g, (_, letter) => letter.toUpperCase())
}

export const kebabCase = (str: string): string => {
  return str.replace(/([a-z0-9])([A-Z])/g, '$1-$2').toLowerCase()
}
