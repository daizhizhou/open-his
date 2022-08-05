import request from '@/utils/request'

// export function uploadImage(query) {
//   return request({
//     url: '/system/upload/doUploadImage2',
//     method: 'post',
//     params: query
//   })
// }

export function deleteImage(query) {
  return request({
    url: '/system/upload/deleteImage',
    method: 'post',
    params: query
  })
}
