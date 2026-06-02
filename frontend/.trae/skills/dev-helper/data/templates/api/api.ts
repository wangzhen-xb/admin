import request from '@/utils/http/axios';

// Define API types
export interface {{ apiName }}Request {
  // Add request types here
}

export interface {{ apiName }}Response {
  // Add response types here
}

// API service
export const {{ apiName }}Api = {
  // Example: get data
  getData: (params?: {{ apiName }}Request) => {
    return request.get<{{ apiName }}Response>({
      url: '/api/{{ apiNameLower }}',
      params,
    });
  },

  // Example: create data
  createData: (data: {{ apiName }}Request) => {
    return request.post<{{ apiName }}Response>({
      url: '/api/{{ apiNameLower }}',
      data,
    });
  },

  // Example: update data
  updateData: (id: string, data: {{ apiName }}Request) => {
    return request.put<{{ apiName }}Response>({
      url: `/api/{{ apiNameLower }}/${id}`,
      data,
    });
  },

  // Example: delete data
  deleteData: (id: string) => {
    return request.delete<{{ apiName }}Response>({
      url: `/api/{{ apiNameLower }}/${id}`,
    });
  },
};
